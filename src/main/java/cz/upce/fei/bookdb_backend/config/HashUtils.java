package cz.upce.fei.bookdb_backend.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HashUtils {
    @Value("${jwt.secret}")
    private String secretString;
    private Algorithm algorithm;

    public Algorithm currentAlgorithm() {
        if(algorithm == null) {
            algorithm = Algorithm.HMAC256(secretString.getBytes());
        }

        return algorithm;
    }
}
