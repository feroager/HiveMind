package com.example.support;

abstract public class User
{
    private String login;
    private String password;
    private String mail;

    public User(String login, String password, String mail)
    {
        this.login = login;
        this.password = password;
        this.mail = mail;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }
}
