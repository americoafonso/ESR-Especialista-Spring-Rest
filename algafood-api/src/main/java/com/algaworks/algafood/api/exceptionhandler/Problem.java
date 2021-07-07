package com.algaworks.algafood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL) //~> Propriedades com valores null nao estarao no corpo da resposta.
@Data
@Builder
public class Problem {

    private Integer Status;
    private String type;
    private String title;
    private String detail;
}
