package com.pumplog.PumpLog.service;

import com.pumplog.PumpLog.config.jwt.JwtUtils;
import com.pumplog.PumpLog.dto.JwtDTO;
import com.pumplog.PumpLog.dto.UserDTO;
import com.pumplog.PumpLog.mapper.UserMapper;
import com.pumplog.PumpLog.model.User;
import com.pumplog.PumpLog.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class UserService {

    // Regex per validare un'email:
    // ^                        -> Inizio della stringa
    // [A-Za-z0-9+_.-]+         -> Nome utente dell'email:
    //    A-Z                   -> Lettere maiuscole (A-Z)
    //    a-z                   -> Lettere minuscole (a-z)
    //    0-9                   -> Numeri (0-9)
    //    +                     -> Carattere più (+)
    //    _                     -> Carattere underscore (_)
    //    .                     -> Punto (.)
    //    -                     -> Trattino (-)
    // @                        -> Simbolo chiocciola (@) (obbligatorio)
    // [A-Za-z0-9.-]+           -> Dominio dell'email:
    //    A-Z                   -> Lettere maiuscole (A-Z)
    //    a-z                   -> Lettere minuscole (a-z)
    //    0-9                   -> Numeri (0-9)
    //    .                     -> Punto (.)
    //    -                     -> Trattino (-)
    // \\.[a-z]{2,}             -> Estensione del dominio (deve essere):
    //    .                     -> Punto (.)
    //    a-z                   -> Lettere minuscole (a-z)
    //    {2,}                  -> Almeno due lettere minuscole
    // $                        -> Fine della stringa
    final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";

    // Regex per validare un username:
    // ^                        -> Inizio della stringa
    // [A-Za-z0-9._-]+          -> Nome utente:
    //    A-Z                   -> Lettere maiuscole (A-Z)
    //    a-z                   -> Lettere minuscole (a-z)
    //    0-9                   -> Numeri (0-9)
    //    .                     -> Punto (.)
    //    _                     -> Underscore (_)
    //    -                     -> Trattino (-)
    // {3,15}                   -> Lunghezza da 3 a 15 caratteri
    // $                        -> Fine della stringa
    final String REGEX_USERNAME = "^[A-Za-z0-9._-]{3,15}$";

    // Regex per validare una password:
    // ^                        -> Inizio della stringa
    // (?=.*[a-z])              -> Almeno una lettera minuscola
    // (?=.*[A-Z])              -> Almeno una lettera maiuscola
    // (?=.*\\d)                -> Almeno un numero
    // (?=.*[@$!%*?&_])         -> Almeno un carattere speciale
    // [A-Za-z\\d@$!%*?&_]{8,}  -> Almeno 8 caratteri, composto da lettere, numeri e caratteri speciali
    // $                        -> Fine della stringa
    final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$";

    // BCrypt eseguirà 2^12 iterazioni per criptare la password
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;


    public ResponseEntity<String> registration(UserDTO userDTO){

        log.info("[UserService registration] Start validation");

        if(userDTO == null) {
            log.error("[UserService registration] UserDTO is null");
            return ResponseEntity.badRequest().body("UserDTO is null");
        }

        String email = userDTO.getEmail();
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        if(StringUtils.isEmpty(email)) {
            log.error("[UserService registration] Missing email");
            return ResponseEntity.badRequest().body("Missing Email");
        }
        if(StringUtils.isEmpty(username)) {
            log.error("[UserService registration] Missing username");
            return ResponseEntity.badRequest().body("Missing Username");
        }
        if(StringUtils.isEmpty(password)) {
            log.error("[UserService registration] Missing password");
            return ResponseEntity.badRequest().body("Missing Password");
        }

        if(isValid(email, REGEX_EMAIL)){
            log.error("[UserService registration] Invalid Email");
            return ResponseEntity.badRequest().body("Invalid Email");
        }
        if(isValid(username, REGEX_USERNAME)){
            log.error("[UserService registration] Invalid Username");
            return ResponseEntity.badRequest().body("Invalid Username");
        }
        if(isValid(password, REGEX_PASSWORD)) {
            log.error("[UserService registration] Invalid Password");
            return ResponseEntity.badRequest().body("Invalid Password");
        }

        if(userRepository.findByEmail(email) != null) {
            log.error("[UserService registration] Email already exists");
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if(userRepository.findByUsername(username) != null) {
            log.error("[UserService registration] Username already exists");
            return ResponseEntity.badRequest().body("Username already exists");
        }

        log.info("[UserService registration] End validation");

        User user = userMapper.toEntity(userDTO);
        user.setPassword(encoder.encode(password));

        userRepository.save(user);

        log.info("[UserService registration] User successfully registered. {}", user);
        return ResponseEntity.ok("User successfully registered. \n" + user);
    }

    public ResponseEntity<String> login(UserDTO userDTO){

        log.info("[UserService login] Start validation");

        if(userDTO == null) {
            log.error("[UserService login] UserDTO is null");
            return ResponseEntity.badRequest().body("UserDTO is null");
        }

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        if(StringUtils.isEmpty(username)) {
            log.error("[UserService login] Missing username");
            return ResponseEntity.badRequest().body("Missing Username");
        }
        if(StringUtils.isEmpty(password)) {
            log.error("[UserService login] Missing password");
            return ResponseEntity.badRequest().body("Missing Password");
        }

        User user = userRepository.findByUsername(username);
        if(user == null) {
            log.error("[UserService login] User not found");
            return ResponseEntity.badRequest().body("User not found");
        }

        if(!encoder.matches(password, user.getPassword())) {
            log.error("[UserService login] Invalid Password");
            return ResponseEntity.badRequest().body("Invalid Password");
        }

        log.info("[UserService login] End validation");

        JwtDTO jwtDTO = jwtUtils.generateToken(username);

        log.info("[UserService login] User successfully logged in. Token: {} \nCreated at {} \nExpires at {} ", jwtDTO.getToken(), jwtDTO.getCreatedAt(), jwtDTO.getExpiresAt());
        return ResponseEntity.ok("User successfully logged in. \nToken: " + jwtDTO.getToken() + "\nCreated at " + jwtDTO.getCreatedAt() + "\nExpires at " + jwtDTO.getExpiresAt());
    }

    private boolean isValid(String str, String REGEX){
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(str);
        return !matcher.matches();
    }

}
