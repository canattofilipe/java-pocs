package br.com.alura.logs.service;

import br.com.alura.logs.model.CursoModel;
import br.com.alura.logs.repository.CursoRepository;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

  final CursoRepository cursoRepository;

  public CursoService(CursoRepository cursoRepository) {
    this.cursoRepository = cursoRepository;
  }

  @Transactional
  public CursoModel save(CursoModel cursoModel) {
    return cursoRepository.save(cursoModel);
  }

  public boolean existsByNumeroMatricula(String numeroMatricula) {
    return cursoRepository.existsByNumeroMatricula(numeroMatricula);
  }

  public boolean existsByNumeroCurso(String numeroCurso) {
    return cursoRepository.existsByNumeroCurso(numeroCurso);
  }

  public Page<CursoModel> findAll(Pageable pageable) {
    return cursoRepository.findAll(pageable);
  }

  public Optional<CursoModel> findById(UUID id) {
    return cursoRepository.findById(id);
  }

  @Transactional
  public void delete(CursoModel cursoModel) {
    cursoRepository.delete(cursoModel);
  }
}
