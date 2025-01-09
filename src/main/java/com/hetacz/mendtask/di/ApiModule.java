package com.hetacz.mendtask.di;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.hetacz.mendtask.api.ApiService;
import com.hetacz.mendtask.service.ConfigService;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApiModule extends AbstractModule {

    @Provides
    @Singleton
    public static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    public static Request.Builder provideRequestBuilder() {
        return new Request.Builder();
    }

    @Provides
    @Singleton
    public static ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public static JsonFactory provideJsonFactory() {
        return JsonFactory.builder().build();
    }

    @Override
    protected void configure() {
        bind(ConfigService.class).in(Singleton.class);
        bind(ApiService.class).in(Singleton.class);
    }
}
