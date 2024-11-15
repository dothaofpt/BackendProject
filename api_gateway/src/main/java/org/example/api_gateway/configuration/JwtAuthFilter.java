package org.example.api_gateway.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Value("${jwt.secret}")
    private String secretKey = "mySuperSecretKey12345678901234567890";

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();


            if (path.contains("/auth/")) {
                return chain.filter(exchange);
            }


            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return this.onError(exchange, "Missing authorization header", 401);
            }

            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return this.onError(exchange, "Invalid authorization header", 401);
            }

            String token = authorizationHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                        .parseClaimsJws(token)
                        .getBody();
                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);


                if (path.contains("/products") || path.contains("/categories")) {
                    if (!roles.contains("ADMIN")) {
                        return this.onError(exchange, "Access denied: Admin role required", 403);
                    }
                }


                exchange.getRequest().mutate()
                        .header("username", username)
                        .header("roles", String.join(",", roles))
                        .build();

            } catch (SignatureException e) {
                return this.onError(exchange, "Invalid JWT signature", 401);
            } catch (Exception e) {
                return this.onError(exchange, "JWT token parsing error", 401);
            }



            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, int httpStatus) {
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.valueOf(httpStatus));
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}
