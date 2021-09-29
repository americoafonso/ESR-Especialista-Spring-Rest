package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.Groups;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.findAll();
    }

    @GetMapping("/{restauranteId}")
    public Restaurante buscar(@PathVariable("restauranteId") Long id) {
        return cadastroRestauranteService.buscarOuFalhar(id);
    }

//    @GetMapping("/{restauranteId}")
//    public ResponseEntity<Restaurante> buscar(@PathVariable("restauranteId") Long id) {
//        Optional<Restaurante> restaurante = restauranteRepository.findById(id);
//
//        return restaurante.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()); // ~> esta linha faz a mesma coisa que a validacao a baixo.
//
//        if (restaurante.isPresent()) {
//            return ResponseEntity.ok(restaurante.get());
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar(@RequestBody @Valid Restaurante restaurante) {
        try {
        return cadastroRestauranteService.salvar(restaurante);
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
//        try {
//            restaurante = cadastroRestauranteService.salvar(restaurante);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
//        } catch (EntidadeNaoEncontradaException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PutMapping("/{restauranteId}")
    public Restaurante atualizar(@PathVariable("restauranteId") Long id, @RequestBody @Valid Restaurante restaurante) {
        Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(id);

        BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");

        try {
            return cadastroRestauranteService.salvar(restauranteAtual);
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

//    @PutMapping("/{restauranteId}")
//    public ResponseEntity<?> atualizar(@PathVariable("restauranteId") Long id, @RequestBody Restaurante restaurante) {
//        try {
//            Restaurante restauranteAtual = restauranteRepository.findById(id).orElse(null);
//
//            if (restauranteAtual != null) {
//                BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
//                Restaurante restauranteSalvo = cadastroRestauranteService.salvar(restauranteAtual);
//                return ResponseEntity.ok(restauranteSalvo);
//            }
//            return ResponseEntity.notFound().build();
//
//        } catch (EntidadeNaoEncontradaException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }

    @PatchMapping("/{restauranteId}")
    public Restaurante atualizarParcial(@PathVariable("restauranteId") Long id, @RequestBody Map<String, Object> campos, HttpServletRequest request) {
         Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(id);

         merge(campos, restauranteAtual, request);

         return atualizar(id, restauranteAtual);
    }

//    @PatchMapping("/{restauranteId}")
//    public ResponseEntity<?> atualizaParcial(@PathVariable("restauranteId") Long restauranteId, @RequestBody Map<String, Object> campos) {
//        Restaurante restauranteAtual = restauranteRepository.findById(restauranteId).orElse(null);
//
//        if (restauranteAtual == null) {
//            return ResponseEntity.notFound().build();
//        }
//        merge(campos, restauranteAtual);
//
//        return atualizar(restauranteId, restauranteAtual);
//    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino,
                       HttpServletRequest request) {
        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

            dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
                Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
                field.setAccessible(true);

                Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

                ReflectionUtils.setField(field, restauranteDestino, novoValor);
            });
        } catch (IllegalArgumentException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
        }
    }

}
