package com.GenCin.GenCin.Security;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.GenCin.GenCin.Sessao.SessaoService;

@Component
public class BearerTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SessaoService sessaoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Aplica a validação somente para o endpoint /api/v1/usuario/getinfo
        if ("/api/v1/usuario/getinfo".equals(request.getServletPath())) {
            String authHeader = request.getHeader("Authorization");
            String keySessao = request.getParameter("keySessao");

            System.out.println("keySessao: " + keySessao);
            System.out.println("Authorization header: " + authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization header is missing or invalid.");
                return;
            }

            if (keySessao == null || keySessao.isEmpty()) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "keySessao parameter is missing.");
                return;
            }

            // Extrai o token removendo o prefixo "Bearer "
            String bearerToken = authHeader.substring(7);

            boolean valid = sessaoService.validarBearerToken(keySessao, bearerToken);
            if (!valid) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token is invalid or does not match keySessao.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
