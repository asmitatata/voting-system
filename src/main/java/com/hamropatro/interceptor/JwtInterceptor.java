package com.hamropatro.interceptor;

import com.hamropatro.entity.UserEntity;
import com.hamropatro.enums.Role;
import com.hamropatro.jwt.JwtValidator;
import io.grpc.*;
import io.jsonwebtoken.Claims;
import jakarta.inject.Singleton;

import java.util.Set;
import java.util.UUID;

@Singleton
public class JwtInterceptor implements ServerInterceptor {
    private final JwtValidator jwtValidator;

    public JwtInterceptor(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    private static final Set<String> PUBLIC_METHODS = Set.of(
            "com.hamropatro.AuthService/Login",
            "com.hamropatro.AuthService/Register",
            "com.hamropatro.AuthService/LoginRefresh",
            "com.hamropatro.AuthService/GoogleLogin"
    );

    private static final Metadata.Key<String> AUTHORIZATION =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String method = call.getMethodDescriptor().getFullMethodName();

        // Public endpoints
        if (PUBLIC_METHODS.contains(method)) {
            return next.startCall(call, headers);
        }

        // Authentication
        String authHeader = headers.get(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            call.close(
                    Status.UNAUTHENTICATED.withDescription("Missing access token"),
                    new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        String token = authHeader.substring(7);

        try {
            // 3. Validate JWT
            Claims claims =
                    jwtValidator.validateAccessToken(token);

            // 4. Extract claims
            UserEntity user = new UserEntity(
                    UUID.fromString(claims.getSubject()),
                    claims.get("email", String.class),
                    Role.valueOf(claims.get("role", String.class))
            );

            // 5. Put authenticated user into Context
            Context context = Context.current()
                    .withValue(AuthContext.USER, user);

            // 6. Continue request
            return Contexts.interceptCall(
                    context,
                    call,
                    headers,
                    next
            );
        } catch (Exception ex) {

            ex.printStackTrace();
            call.close(
                    Status.UNAUTHENTICATED.withDescription("Invalid or expired token"),
                    new Metadata());

            return new ServerCall.Listener<>() {
            };
        }
    }
}
