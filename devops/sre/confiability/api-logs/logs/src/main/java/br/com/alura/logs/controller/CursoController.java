package br.com.alura.logs.controller;

import br.com.alura.logs.dto.CursoDto;
import br.com.alura.logs.model.CursoModel;
import br.com.alura.logs.service.CursoService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cursos")
public class CursoController {

  final CursoService cursoService;

  public CursoController(CursoService cursoService) {
    this.cursoService = cursoService;
  }

  @PostMapping
  public ResponseEntity<Object> saveCurso(@RequestBody @Valid CursoDto cursoDto) {

    if (cursoService.existsByNumeroMatricula(cursoDto.getNumeroMatricula())) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("O número de matricula do curso já esta em uso!");
    }

    if (cursoService.existsByNumeroCurso(cursoDto.getNumeroCurso())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("O número do curso já esta em uso!");
    }

    var cursoModel = new CursoModel();
    BeanUtils.copyProperties(cursoDto, cursoModel);
    cursoModel.setDataInscricao(LocalDateTime.now(ZoneId.of("UTC")));
    return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(cursoModel));
  }

  @GetMapping
  public ResponseEntity<Page<CursoModel>> getAllCursos(
      @PageableDefault(page = 0, size = 10, sort = "dataInscricao", direction = Sort.Direction.ASC)
          Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(cursoService.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getOneCursos(@PathVariable(value = "id") UUID id) {
    Optional<CursoModel> cursoModelOptional = cursoService.findById(id);
    if (!cursoModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado!");
    }
    return ResponseEntity.status(HttpStatus.OK).body(cursoModelOptional.get());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteCursos(@PathVariable(value = "id") UUID id) {
    Optional<CursoModel> cursoModelOptional = cursoService.findById(id);
    if (!cursoModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado!");
    }
    cursoService.delete(cursoModelOptional.get());
    return ResponseEntity.status(HttpStatus.OK).body("Curso excluído com sucesso!");
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateCursos(
      @PathVariable(value = "id") UUID id, @RequestBody @Valid CursoDto cursoDto) {
    Optional<CursoModel> cursoModelOptional = cursoService.findById(id);
    if (!cursoModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso não encontrado!");
    }
    var cursoModel = new CursoModel();
    BeanUtils.copyProperties(cursoDto, cursoModel);
    cursoModel.setId(cursoModelOptional.get().getId());
    cursoModel.setDataInscricao(cursoModelOptional.get().getDataInscricao());
    return ResponseEntity.status(HttpStatus.OK).body(cursoService.save(cursoModel));
  }
}
