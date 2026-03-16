package com.unamba.matriculas.dto;

public class AuthResult {
    private Object user; // Administrador or Estudiante
    private String tipoPersona; // 'ADMIN' or 'ESTUDIANTE'
    private String token;

    public AuthResult(Object user, String tipoPersona, String token) {
        this.user = user;
        this.tipoPersona = tipoPersona;
        this.token = token;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
