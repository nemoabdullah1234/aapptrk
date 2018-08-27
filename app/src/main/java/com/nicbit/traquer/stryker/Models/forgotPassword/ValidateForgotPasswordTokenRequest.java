package com.nicbit.traquer.stryker.Models.forgotPassword;

public class ValidateForgotPasswordTokenRequest {


    String email = null;

    String token = null;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}