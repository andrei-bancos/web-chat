package com.andreibancos.webchatbackend.service;

import com.andreibancos.webchatbackend.entity.User;
import com.andreibancos.webchatbackend.repository.IUserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;

    public AuthService(IUserRepository IUserRepository) {
        this.userRepository = IUserRepository;
    }

    @PostConstruct
    public void init() {
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getBytes()));
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(
                new SecretKeySpec(jwtSecretKey.getBytes(), "HmacSHA256")
        ).build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found")
        );

        return org.springframework.security.core
                .userdetails
                .User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public String createJwtToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24 * 3600))
                .subject(user.getUsername())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .build();

        var params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return jwtEncoder.encode(params).getTokenValue();
    }

    public Jwt validateJwtToken(String token) {
        return jwtDecoder.decode(token);
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromJwt(Jwt jwt) {
        var roles = jwt.getClaimAsStringList("roles");
        return roles != null
                ? roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                : List.of();
    }
}
