package com.ejemplo.insert.database.oauth2udemyretrofitjava.model;

public class MovimientoBancario {
    private Long userId;
    private String importe;//moneda de mi pais
    private String name;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
