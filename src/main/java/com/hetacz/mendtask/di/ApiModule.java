package com.hetacz.mendtask.di;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import okhttp3.OkHttpClient;

public class ApiModule extends AbstractModule {

    @Provides
    @Singleton
    public ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }
}
