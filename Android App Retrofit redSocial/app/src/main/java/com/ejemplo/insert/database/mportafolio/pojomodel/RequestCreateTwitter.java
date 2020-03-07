package com.ejemplo.insert.database.mportafolio.pojomodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCreateTwitter {

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    /**
     * No args constructor for use in serialization
     */
    public RequestCreateTwitter() {
    }

    /**
     * @param mensaje
     */
    public RequestCreateTwitter(String mensaje) {
        super();
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
