package pl.lipinski.settlers_deckbuilder.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.entity.User;
import pl.lipinski.settlers_deckbuilder.repository.UserRepository;
import pl.lipinski.settlers_deckbuilder.security.UserDetailsImpl;
import pl.lipinski.settlers_deckbuilder.util.enums.Role;
import pl.lipinski.settlers_deckbuilder.util.exception.JWTException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

import javax.security.sasl.AuthenticationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.CAN_NOT_FIND_USER_BY_ID_ERROR_CODE;

@Service
public class JwtUtil {

    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(User user) {
        LocalDateTime dateTime = LocalDateTime.now();

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(dateTime.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS256, "hOK21~-aa02kld.wqj2rWJENEnww90-a11".getBytes())
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthenticationByToken(String token)
            throws AuthenticationException, JWTException, UserNotFoundException {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString("hOK21~-aa02kld.wqj2rWJENEnww90-a11".getBytes()))
                    .parseClaimsJws(token.replace("Bearer ", ""));
        } catch (Exception e) {
            throw new JWTException(CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE.getValue());
        }

        Long userId = Long.valueOf(claimsJws.getBody().get("sub").toString());
        String userEmail = claimsJws.getBody().get("email").toString();
        String userRole = claimsJws.getBody().get("role").toString();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId, CAN_NOT_FIND_USER_BY_ID_ERROR_CODE.getValue()));

        if (!user.getEmail().equals(userEmail) ||
                !user.getRole().equals(Role.valueOf(userRole))) {
            throw new AuthenticationException();
        }

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(userRole));
        UserDetailsImpl userPrincipal = new UserDetailsImpl(user);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, simpleGrantedAuthorities);
    }
}
