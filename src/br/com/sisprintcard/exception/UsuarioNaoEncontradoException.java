package br.com.sisprintcard.exception;


import java.lang.reflect.InvocationTargetException;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException (String msg) {
        super( msg);
    }
}
