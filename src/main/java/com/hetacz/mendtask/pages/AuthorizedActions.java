package com.hetacz.mendtask.pages;

import org.openqa.selenium.Cookie;

import java.util.List;
import java.util.Set;

public interface AuthorizedActions {

    AuthorizedActions load();

    String confirmLogin();

    List<String> getRepositories();

    Set<Cookie> extractCookies();
}
