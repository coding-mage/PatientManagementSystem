package com.pm.apigateway.filter;

import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtValidationGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(JwtValidationGatewayFilterFactory.class);
    private final WebClient webClient;

    public static class Config {
        private String roles;

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }
    }

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder, @Value("${auth-service.url}") String authServiceUrl) {
        super(Config.class);
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public java.util.List<String> shortcutFieldOrder() {
        return java.util.Arrays.asList("roles");
    }

    @Override
    public GatewayFilter apply(Config config){
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toEntity(Void.class)
                    .flatMap(responseEntity -> {
                        String userRole = responseEntity.getHeaders().getFirst("X-User-Role");
                        String userEmail = responseEntity.getHeaders().getFirst("X-User-Email");

                        if (config.getRoles() != null && !config.getRoles().isEmpty()) {
                            java.util.List<String> allowed = java.util.Arrays.asList(config.getRoles().split("[,;]"));
                            if (userRole == null || !allowed.contains(userRole)) {
                                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                return exchange.getResponse().setComplete();
                            }
                        }

                        return chain.filter(exchange.mutate()
                                .request(r -> r.headers(h -> {
                                    if (userRole != null) h.set("X-User-Role", userRole);
                                    if (userEmail != null) h.set("X-User-Email", userEmail);
                                }))
                                .build());
                    })
                    .onErrorResume(e -> {
                        logger.error("JWT Validation Filter Error", e);
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }
}
