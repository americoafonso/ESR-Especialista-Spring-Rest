package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> litar() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{estadoId}")
    public Estado buscar(@PathVariable Long estadoId) {
        return cadastroEstadoService.buscarOuFalhar(estadoId);
    }

//    @GetMapping("/{estadoId}")
//    public ResponseEntity<Estado> buscar(@PathVariable("estadoId") Long id) {
//        Optional<Estado> estado = estadoRepository.findById(id);
//
//        if (estado.isPresent()) {
//            return ResponseEntity.ok(estado.get());
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@RequestBody @Valid Estado estado) {
        return cadastroEstadoService.salvar(estado);
    }

    @PutMapping("/{estadoId}")
    public Estado atualizar(@PathVariable Long estadoId, @RequestBody @Valid Estado estado) {
        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(estadoId);

        BeanUtils.copyProperties(estado, estadoAtual, "id");

        return cadastroEstadoService.salvar(estadoAtual);
    }

//    @PutMapping("/{estadoId}")
//    public ResponseEntity<Estado> atualizar(@PathVariable("estadoId") Long id, @RequestBody  Estado estado) {
//        Optional<Estado> estadoAtual = estadoRepository.findById(id);
//
//        if (estadoAtual.isPresent()) {
//            BeanUtils.copyProperties(estado, estadoAtual.get(), "id");
//            Estado estadoSalvo = cadastroEstadoService.salvar(estadoAtual.get());
//            return ResponseEntity.ok(estadoSalvo);
//        }
//        return ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{estadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long estadoId) {
        cadastroEstadoService.excluir(estadoId);
    }

//    @DeleteMapping("/{estadoId}")
//    public ResponseEntity<?> remover(@PathVariable("estadoId") Long id) {
//        try {
//            cadastroEstadoService.excluir(id);
//            return ResponseEntity.noContent().build();
//
//        }catch (EntidadeNaoEncontradaException e) {
//            return ResponseEntity.notFound().build();
//
//        }catch (EntidadeEmUsoException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        }
//    }

}
