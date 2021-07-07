package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    @GetMapping("/{cidadeId}")
    public Cidade buscar(@PathVariable("cidadeId") Long id) {
        return cadastroCidadeService.buscarOuFalhar(id);
    }

//    @GetMapping("/{cidadeId}")
//    public ResponseEntity<Cidade> buscar(@PathVariable("cidadeId") Long id) {
//        Optional<Cidade> cidade = cidadeRepository.findById(id);
//
//        if (cidade.isPresent()) {
//            return ResponseEntity.ok(cidade.get());
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade adicionar(@RequestBody Cidade cidade) {
        try {
        return cadastroCidadeService.salvar(cidade);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
//        try {
//            cidade = cadastroCidadeService.salvar(cidade);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
//        }catch (EntidadeNaoEncontradaException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PutMapping("/{cidadeId}")
    public Cidade atualizar(@PathVariable("cidadeId") Long id, @RequestBody Cidade cidade) {

        try {
            Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(id);

            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            return cadastroCidadeService.salvar(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

//    @PutMapping("/{cidadeId}")
//    public ResponseEntity<?> atualizar(@PathVariable("cidadeId") Long id, @RequestBody Cidade cidade) {
//        try {
//            Optional<Cidade> cidadeAtual = cidadeRepository.findById(id);
//
//            if (cidadeAtual.isPresent()) {
//                BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");
//                Cidade cidadeSalva = cadastroCidadeService.salvar(cidadeAtual.get());
//                return ResponseEntity.ok(cidadeSalva);
//            }
//            return ResponseEntity.notFound().build();
//
//        } catch (EntidadeNaoEncontradaException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidadeService.excluir(cidadeId);
    }

//    @DeleteMapping("/{cidadeId}")
//    public ResponseEntity<Cidade> remover(@PathVariable("cidadeId") Long id) {
//        try {
//            cadastroCidadeService.excluir(id);
//            return ResponseEntity.noContent().build();
//
//        } catch (EntidadeNaoEncontradaException e) {
//            return ResponseEntity.notFound().build();
//
//        } catch (EntidadeEmUsoException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//    }
}
