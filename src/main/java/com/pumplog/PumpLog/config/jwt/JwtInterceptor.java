package com.pumplog.PumpLog.config.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pumplog.PumpLog.exception.JwtValidationException;
import com.pumplog.PumpLog.model.User;
import com.pumplog.PumpLog.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Base64;

@Aspect
@Log4j2
@Configuration
public class JwtInterceptor {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    // questo metodo deve essere eseguito prima dei metodi annotati con @ValidateJwt e classi annotate con @RestController
    @Before("@within(org.springframework.web.bind.annotation.RestController) && @annotation(com.pumplog.PumpLog.config.jwt.ValidateJwt)")
    public void validateJwt() throws Exception {

        String authHeader = request.getHeader("Authorization");                                                                     // ottiene l'header della richiesta
        if(StringUtils.isEmpty(authHeader)) {
            log.error("[JwtInterceptor validateJwt()] Missing auth header");
            throw new JwtValidationException("Missing auth header");
        }
        log.info("[JwtInterceptor validateJwt()] authHeader: {}", authHeader);

        String token = authHeader.replace("Bearer ", "");                                                            // rimuove il prefisso Bearer
        if (StringUtils.isEmpty(token)) {
            log.error("[JwtInterceptor validateJwt()] Missing jwt token");
            throw new JwtValidationException("Missing jwt token");
        }

        if (!token.contains(".") || token.split("\\.").length != 3) {                                                           // controlla se il token ha il formato corretto (tre parti separate da punti)
            log.error("[JwtInterceptor validateJwt()] Invalid jwt token");
            throw new JwtValidationException("Invalid jwt token");
        }

        if (jwtUtils.isTokenExpired(token)) {
            log.error("[JwtInterceptor validateJwt()] Expired jwt token");
            throw new ExpiredJwtException(null, null, null);                                                                         // controlla se il token non è scaduto
        }

        log.info("[JwtInterceptor validateJwt()] token: {}", token);

        String[] chunks = token.split("\\.");

        log.info("[JwtInterceptor validateJwt()] chunks: {} ", Arrays.toString(chunks));

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String chunk2 = new String(decoder.decode(chunks[1]));                                                                        // decodifica il secondo chunck (il payload)

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(chunk2, JsonNode.class);                                                                 // converte il payload in un oggetto JSON

        log.info("[JwtInterceptor validateJwt()] decoded jwt payload: {}", chunk2);

        String username = jsonNode.get("sub").asText();                                                                               // estrae il nome utente

        if (StringUtils.isEmpty(username)) {
            log.error("[JwtInterceptor validateJwt()] Missing username in jwt payload");
            throw new JwtValidationException("Missing username in jwt payload");
        }

        User user = userRepository.findByUsername(username);                                                                          // controlla se il nome utente esiste

        if (user == null) {
            log.error("[JwtInterceptor validateJwt()] User not present");
            throw new JwtValidationException("User not present");
        }

        request.setAttribute("username", username);                                                                                // aggiunge l'username alla richiesta

        log.info("[JwtInterceptor validateJwt()] validation successful for user: {}", username);
    }
}
