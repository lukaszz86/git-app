package com.gitapp.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Slf4j
@Configuration
public class Config {

    @Bean
    public WebClient.Builder webClient() throws SSLException {
        return WebClient.builder()
                .clientConnector(sslConnector());
    }

    private ReactorClientHttpConnector sslConnector() throws SSLException{
        SslContext sslContext = SslContextBuilder.forClient().build();
        HttpClient httpClient = HttpClient.create().secure(sslSpec -> sslSpec.sslContext(sslContext));
        return new ReactorClientHttpConnector(httpClient);
    }
}
