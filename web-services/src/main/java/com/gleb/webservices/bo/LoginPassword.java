package com.gleb.webservices.bo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginPassword {
    private String login;
    private String passwordBase64;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordBase64() {
        return passwordBase64;
    }

    public void setPasswordBase64(String passwordBase64) {
        this.passwordBase64 = passwordBase64;
    }

}
