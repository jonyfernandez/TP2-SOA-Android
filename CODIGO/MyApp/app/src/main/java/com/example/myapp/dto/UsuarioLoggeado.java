package com.example.myapp.dto;

public class UsuarioLoggeado {

    private static String token;
    private static String token_refresh;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UsuarioLoggeado.token = token;
    }

    public static String getToken_refresh() {
        return token_refresh;
    }

    public static void setToken_refresh(String token_refresh) {
        UsuarioLoggeado.token_refresh = token_refresh;
    }
}
