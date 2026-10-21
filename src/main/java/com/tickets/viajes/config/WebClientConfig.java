package com.tickets.viajes.config;

import com.tickets.viajes.clients.ClienteClient;
import com.tickets.viajes.clients.ConductorClient;
import com.tickets.viajes.clients.ParaderoClient;
import com.tickets.viajes.clients.UserClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ParaderoClient getParaderos(WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://PARADEROS").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return factory.createClient(ParaderoClient.class);
    }

    @Bean
    public UserClient getUserClient(WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://USUARIOS").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return factory.createClient(UserClient.class);
    }

    @Bean
    public ClienteClient getCliente(WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://CLIENTES").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return factory.createClient(ClienteClient.class);
    }

    @Bean
    public ConductorClient getConductor(WebClient.Builder builder) {
        WebClient webClient = builder
                .baseUrl("http://CONDUCTORES").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return factory.createClient(ConductorClient.class);
    }
}
