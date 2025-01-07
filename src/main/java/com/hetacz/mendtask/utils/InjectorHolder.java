package com.hetacz.mendtask.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hetacz.mendtask.di.SeleniumModule;
import com.hetacz.mendtask.service.ConfigService;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InjectorHolder {

    public ConfigService getConfigService() {
        return getInjector().getInstance(ConfigService.class);
    }

    private Injector getInjector() {
        if (Holder.INJECTOR == null) {
            throw new IllegalStateException("Injector not initialized!");
        }
        return Holder.INJECTOR;
    }

    private enum Holder {
        ;

        static final Injector INJECTOR = Guice.createInjector(new SeleniumModule());
    }
}
