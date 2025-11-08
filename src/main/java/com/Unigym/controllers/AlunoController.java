package com.Unigym.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.Unigym.entities.Aluno;
import com.Unigym.repositories.AlunoRepository;
import com.Unigym.repositories.MedidaRepository;
import com.Unigym.repositories.TreinoRepository;

/**
 * AlunoController
 * 
 * Este controlador gerencia todas as operações relacionadas aos alunos no sistema UniGym,
 * expondo endpoints REST para listar, buscar, criar, atualizar e remover alunos.
 * 
 * Endpoint base: /alunos
 * 
 * Funcionalidades:
 * - Listar todos os alunos
 * - Buscar aluno por ID
 * - Criar novo aluno
 * - (Potencial extensão: atualizar e remover alunos)
 * 
 * Este controlador utiliza o AlunoRepository para acessar o banco de dados
 * e manipular as entidades de Aluno de acordo com as requisições recebidas.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/alunos") // se eu acessar o localhost:8080/alunos estarei acessando aqui
public class AlunoController {

    private static final Logger log = LoggerFactory.getLogger(AlunoController.class);

    private final AlunoRepository repository;
    private final MedidaRepository medidaRepository;
    private final TreinoRepository treinoRepository;

    public AlunoController(AlunoRepository repository,
                           MedidaRepository medidaRepository,
                           TreinoRepository treinoRepository) {
        this.repository = repository;
        this.medidaRepository = medidaRepository;
        this.treinoRepository = treinoRepository;
    }

    /**
     * Retorna a lista de todos os alunos cadastrados no sistema
     * 
     * Método HTTP: GET
     * URL: /alunos
     * 
     * @return Lista de todos os alunos
     */
    @GetMapping
    public List<Aluno> getAllAlunos() {
        return repository.findAll();
    }

    /**
     * Busca um aluno específico pelo seu ID
     * 
     * Método HTTP: GET
     * URL: /alunos/{id}
     * 
     * @param id Identificador único do aluno
     * @return O aluno correspondente ao ID ou null se não encontrado
     */
    @GetMapping(value = "/{id}")
    public Aluno getAlunoById(@PathVariable long id) {
        return repository.findById(id).orElse(null);
    }
    
    /**
     * Cria um novo aluno no sistema
     * 
     * Método HTTP: POST
     * URL: /alunos
     * Body: Objeto JSON com os dados do aluno
     * 
     * @param aluno Objeto Aluno com os dados a serem persistidos
     * @return O aluno criado com o ID gerado
     */
    @PostMapping
    public Aluno insert(@RequestBody Aluno aluno) {
        // Null safety: Spring guarantees @RequestBody not null when JSON is provided; defensively log otherwise
        if (aluno == null) {
            log.warn("Tentativa de salvar aluno nulo");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aluno inválido");
        }
        return repository.save(aluno);
    }
    
    /**
     * Remove um aluno e seus dados relacionados (medidas e treinos)
     * 
     * Método HTTP: DELETE
     * URL: /alunos/{id}
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        // Garante que o aluno exista
        var alunoOpt = repository.findById(id);
        if (alunoOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }
        Aluno aluno = alunoOpt.get();

        // Remove medidas do aluno (garantindo lista não nula)
        var medidas = medidaRepository.findByAlunoId(id);
        if (medidas != null && !medidas.isEmpty()) {
            medidaRepository.deleteAll(medidas);
        }

        // Remove treinos do aluno (Exercicios serão removidos por cascade no Treino)
        var treinos = treinoRepository.findByAluno(aluno);
        if (treinos != null && !treinos.isEmpty()) {
            treinoRepository.deleteAll(treinos);
        }

    // Por fim, remove o aluno
    repository.deleteById(id);
    }
    
}
