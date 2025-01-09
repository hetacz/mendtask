package com.hetacz.mendtask.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hetacz.mendtask.service.AutConfigService;
import com.hetacz.mendtask.service.ConfigService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiService {

    ConfigService cs;

    public String getApiUrl() {
        return AutConfigService.getProperty(cs.getAut(), "api.url");
    }

    public String getApiToken() {
        return AutConfigService.getProperty(cs.getAut(), "api.token");
    }

    public String getApiVersion() {
        return AutConfigService.getProperty(cs.getAut(), "api.version");
    }

    public String getAcceptHeader() {
        return AutConfigService.getProperty(cs.getAut(), "api.acceptHeader");
    }
}
