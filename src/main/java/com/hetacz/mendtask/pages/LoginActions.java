package com.hetacz.mendtask.pages;

public interface LoginActions {

    LoginActions load();

    AuthorizedActions login(String email, String password);
}
