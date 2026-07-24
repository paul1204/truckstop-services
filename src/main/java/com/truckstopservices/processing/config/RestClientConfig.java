package com.truckstopservices.processing.config;

import com.truckstopservices.processing.client.MerchandiseManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public MerchandiseManager inventoryClient(){
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:9000")
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(MerchandiseManager.class);
    }
}
