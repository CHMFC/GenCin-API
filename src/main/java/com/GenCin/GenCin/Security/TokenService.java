package com.GenCin.GenCin.Security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

// Imports do Nimbus JOSE para criar o JWK
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;

@Service
public class TokenService {

    private final NimbusJwtEncoder jwtEncoder;

    public TokenService(@Value("${jwt.secret}") String secretKey) {
        // Cria a chave secreta usando HmacSHA256
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        // Constrói o JWK com a chave secreta
        JWK jwk = new OctetSequenceKey.Builder(key).build();
        // Cria o JWKSet e o JWKSource
        JWKSet jwkSet = new JWKSet(jwk);
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
        // Inicializa o encoder com o JWKSource
        this.jwtEncoder = new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Gera um token JWT para o usuário informado.
     *
     * @param userId UUID do usuário que será incluído como subject no token.
     * @return O token JWT assinado.
     */
    public String generateToken(UUID userId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("GenCin")                         // Emissor do token
                .issuedAt(now)                            // Data de emissão
                .expiresAt(now.plus(10, ChronoUnit.HOURS))  // Expiração em 10 horas
                .subject(userId.toString())               // Subject: ID do usuário
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(
                        JwsHeader.with(() -> "HS256").build(), claims))
                .getTokenValue();
    }
}
