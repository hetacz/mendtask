package com.hetacz.mendtask.pages;

public interface Loadable<T extends BasePage> {

    T load();
}
