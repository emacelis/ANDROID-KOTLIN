package com.ejemplo.insert.database.oauth2udemyretrofitjava.model;

import com.google.gson.annotations.SerializedName;

public class Token {
    //LA clase TOKEN en retrofit es importante ya que de ella salen dos
    //Token de acceso y Token de Refreso(cuando el token de acceso expira se usa el de refresco)

   /*ACESO*/
    @SerializedName("access_token")
    private String accesToken;

    /*Refresco*/
    @SerializedName("access_token")
    private String refreshToken;

    public String getAccesToken() {
        return accesToken;
    }

    public void setAccesToken(String accesToken) {
        this.accesToken = accesToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
