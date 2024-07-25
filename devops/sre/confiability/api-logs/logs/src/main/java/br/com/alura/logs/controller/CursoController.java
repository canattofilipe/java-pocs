package br.com.alura.logs.controller;

import br.com.alura.logs.dto.CursoDto;
import br.com.alura.logs.exceptions.InternalErrorException;
import br.com.alura.logs.model.CursoModel;
import br.com.alura.logs.service.CursoService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cursos")
@Slf4j
public class CursoController {

  final CursoService cursoService;

  public CursoController(CursoService cursoService) {
    this.cursoService = cursoService;
  }

  @PostMapping
  public ResponseEntity<Object> saveCurso(@RequestBody @Valid CursoDto cursoDto) {

    log.info("Creating a new course");

    log.info("Checking if the register is already in use");
    if (cursoService.existsByNumeroMatricula(cursoDto.getNumeroMatricula())) {
      log.warn("The course register number is already in use");
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("O número de matricula do curso já esta em uso!");
    }

    log.info("Checking if the course number is already in use");
    if (cursoService.existsByNumeroCurso(cursoDto.getNumeroCurso())) {
      log.warn("The course number is already in use");
      return ResponseEntity.status(HttpStatus.CONFLICT).body("O número do curso já esta em uso!");
    }

    log.info("Validations passed, saving the course");

    var cursoModel = new CursoModel();
    BeanUtils.copyProperties(cursoDto, cursoModel);
    cursoModel.setDataInscricao(LocalDateTime.now(ZoneId.of("UTC")));
    final var savedCourse = cursoService.save(cursoModel);
    log.info("Course saved successfully");

    return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
  }

  @GetMapping
  public ResponseEntity<Page<CursoModel>> getAllCursos(
      @PageableDefault(page = 0, size = 10, sort = "dataInscricao", direction = Sort.Direction.ASC)
          Pageable pageable) {
    try {
      log.info("Querying all courses");
      return ResponseEntity.status(HttpStatus.OK).body(cursoService.findAll(pageable));
    } catch (CannotCreateTransactionException e) {
      log.error("Database communication error");
      throw new InternalErrorException("Moment error, try again later");
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getOneCursos(@PathVariable(value = "id") UUID id) {

    log.info("Querying course by id");

    Optional<CursoModel> cursoModelOptional = cursoService.findById(id);

    log.info("Checking if the course exists");
    if (!cursoModelOptional.isPresent()) {
      log.warn("Query by id returned no results");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado!");
    }

    log.info("Query by id returned successfully");
    return ResponseEntity.status(HttpStatus.OK).body(cursoModelOptional.get());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteCursos(@PathVariable(value = "id") UUID id) {

    log.info("Deleting course by id");

    log.info("Checking if the course exists");
    Optional<CursoModel> cursoModelOptional = cursoService.findById(id);
    if (!cursoModelOptional.isPresent()) {
      log.warn("Query by id returned no results");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado!");
    }

    log.info("Course found, deleting");
    cursoService.delete(cursoModelOptional.get());
    return ResponseEntity.status(HttpStatus.OK).body("Curso excluído com sucesso!");
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateCursos(
      @PathVariable(value = "id") UUID id, @RequestBody @Valid CursoDto cursoDto) {

    log.info("Updating course by id");

    log.info("Checking if the course exists");
    Optional<CursoModel> cursoModelOptional = cursoService.findById(id);
    if (!cursoModelOptional.isPresent()) {
      log.warn("Query by id returned no results");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado!");
    }

    log.info("Course found, updating");
    var cursoModel = new CursoModel();
    BeanUtils.copyProperties(cursoDto, cursoModel);
    cursoModel.setId(cursoModelOptional.get().getId());
    cursoModel.setDataInscricao(cursoModelOptional.get().getDataInscricao());

    log.info("Course updated successfully");
    return ResponseEntity.status(HttpStatus.OK).body(cursoService.save(cursoModel));
  }
}
