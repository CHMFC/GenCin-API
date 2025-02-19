package com.GenCin.GenCin.Security;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtConfig {

    // Exemplo de secret key forte – deve ser a mesma usada para gerar os tokens
    private final String secretKey = "vOujlRpK!vT4UeY$?c%5Bj8pZs6DqLfMn";

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}
