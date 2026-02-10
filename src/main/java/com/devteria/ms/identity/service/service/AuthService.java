package com.devteria.ms.identity.service.service;

import com.devteria.ms.identity.service.dto.request.AuthRequest;
import com.devteria.ms.identity.service.dto.request.IntrospectRequest;
import com.devteria.ms.identity.service.dto.response.AuthResponse;
import com.devteria.ms.identity.service.dto.response.IntrospectResponse;
import com.devteria.ms.identity.service.exception.AppException;
import com.devteria.ms.identity.service.exception.ErrorCode;
import com.devteria.ms.identity.service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(verifier);

        // get expired time
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder()
                .valid(verified && expiredTime.after(new Date()))
                .build();
    }

    public AuthResponse authenticated(AuthRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // check password input and password saved in database
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user.getUsername());

        return AuthResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    private String generateToken(String username) {
        // 1. Create the header with the signing algorithm
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // 2. Create the claims set (payload)
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("sean.nguyen") // issue from who
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("custom", "CustomHere")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // 3. Create the signer with the shared secret, sign the JWT
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            // 4. Serialize to a compact, URL-safe string
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
