package com.example.support;

public class UserServer extends User
{
    private long id;
    public UserServer(String login, String password, String mail)
    {
        super(login, password, mail);
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
