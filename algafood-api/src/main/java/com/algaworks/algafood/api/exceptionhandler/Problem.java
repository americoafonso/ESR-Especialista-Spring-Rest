package com.algaworks.algafood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) //~> Propriedades com valores null nao estarao no corpo da resposta.
@Data
@Builder
public class Problem {

    private Integer Status;
    private LocalDateTime timestamp;
    private String type;
    private String title;
    private String detail;
    private String userMessage;
    private List<Object> objects;

    @Data
    @Builder
    public static class Object {
        private String name;
        private String userMessage;
    }
}
