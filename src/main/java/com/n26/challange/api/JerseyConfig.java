package com.n26.challange.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath(JerseyConfig.APPLICATION_PATH)
class JerseyConfig extends ResourceConfig {
    
    static final String APPLICATION_PATH = "/transactionservice"; 

    public JerseyConfig() {
        packages(getClass().getPackage().toString());
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
