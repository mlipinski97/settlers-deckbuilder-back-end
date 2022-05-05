package pl.lipinski.settlers_deckbuilder.filter;

import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lipinski.settlers_deckbuilder.util.JwtUtil;
import pl.lipinski.settlers_deckbuilder.util.exception.JWTException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {

        final String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && !authHeader.equals("")) {
            try {
                UsernamePasswordAuthenticationToken authResult = jwtUtil.getAuthenticationByToken(authHeader);
                authResult.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authResult);
            } catch (NoSuchElementException | UserNotFoundException | JWTException | NullPointerException | AuthenticationException e) {
                throw e;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
