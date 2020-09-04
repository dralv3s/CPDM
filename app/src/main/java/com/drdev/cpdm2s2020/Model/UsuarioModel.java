package com.drdev.cpdm2s2020.Model;

import android.icu.lang.UProperty;

import javax.security.auth.login.LoginException;

public class UsuarioModel {

    public String DisplayName;

    public String IdToken;

    public String Login;

    public String Senha;

    public UsuarioModel() {
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getIdToken() {
        return IdToken;
    }

    public void setIdToken(String idToken) {
        IdToken = idToken;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }
}
