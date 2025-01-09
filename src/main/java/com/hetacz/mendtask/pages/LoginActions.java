package com.hetacz.mendtask.pages;

import org.openqa.selenium.Cookie;

import java.util.Set;

public interface LoginActions {

    LoginActions openLogin();

    AuthorizedActions performLogin();

    AuthorizedActions injectCookies(Set<Cookie> cookies);
}
