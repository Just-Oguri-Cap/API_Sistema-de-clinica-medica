package com.senai.karina.sistema_de_clinica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// quando quebrar alguma regra de negócio (cpf duplicado, status errado etc.), retorna 409
@ResponseStatus(HttpStatus.CONFLICT)
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}