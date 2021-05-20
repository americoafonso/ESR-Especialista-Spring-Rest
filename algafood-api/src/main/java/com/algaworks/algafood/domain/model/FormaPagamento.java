package com.algaworks.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FormaPagamento {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

//    @JsonIgnore
//    @ManyToMany
//    @JoinTable(name = "restaurante_forma_pagamento",
//            joinColumns = @JoinColumn(name = "forma_pagamento_id"),
//            inverseJoinColumns = @JoinColumn(name = "restaurante_id"))
//    private List<Restaurante> restaurantes = new ArrayList<>();
}
