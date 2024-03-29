package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController // ~> esta anotacao ja tem as seguintes anotacoes: @Controller e @ResponseBody
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @GetMapping
    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    @GetMapping("/{cozinhaId}")
    public Cozinha buscar(@PathVariable("cozinhaId") Long cozinhaId) {
        return cadastroCozinhaService.buscarOuFalhar(cozinhaId);
    }

//    @GetMapping("/{cozinhaId}")
//    public ResponseEntity<Cozinha> bucar(@PathVariable("cozinhaId") Long id) {
//        Optional<Cozinha> cozinha = cozinhaRepository.findById(id);
//
//        if (cozinha.isPresent()) {
//            //return ResponseEntity.status(HttpStatus.OK).build(); ~ esta linha retorna o status (200 OK) sem o payload
//            //return ResponseEntity.ok(cozinha); ~ este linha faz a mesma coisa que a linha de baixo
//            return ResponseEntity.status(HttpStatus.OK).body(cozinha.get());
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody @Valid Cozinha cozinha) {
        return cadastroCozinhaService.salvar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public Cozinha atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid Cozinha cozinha) {
        Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);

        BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

        return  cadastroCozinhaService.salvar(cozinhaAtual);
    }

//    @PutMapping("/{cozinhaId}")
//    public ResponseEntity<Cozinha> atualizar(@PathVariable("cozinhaId") Long id, @RequestBody Cozinha cozinha) {
//        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(id);
//
//        if (cozinhaAtual.isPresent()) {
//            //cozinhaAtual.setNome(cozinha.getNome()); ~ se tivesse varias propriedades seria necessario setar cada uma
//            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id"); // O terceiro parametro "id" especifica propriedade a ser ignorada
//            Cozinha cozinhaSalva = cadastroCozinhaService.salvar(cozinhaAtual.get());
//            return ResponseEntity.ok(cozinhaSalva);
//        }
//        return ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cozinhaId) {
        cadastroCozinhaService.excluir(cozinhaId);
    }

    /**
     * Este metodo funciona mas nao sera preciso pois criei uma camada responsavel de exception
     * que e lancada pelo service
     * @param cozinhaId
     */
//    @DeleteMapping("/{cozinhaId}")
//    public ResponseEntity<?> remover(@PathVariable("cozinhaId") Long id) {
//        try {
//            cadastroCozinhaService.excluir(id);
//            return ResponseEntity.noContent().build();
//
//        }catch (EntidadeNaoEncontradaException e) {
//            return ResponseEntity.notFound().build();
//
//        } catch (EntidadeEmUsoException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        }
//    }

}
