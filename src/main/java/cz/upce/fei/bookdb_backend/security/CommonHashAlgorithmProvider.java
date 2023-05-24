package cz.upce.fei.bookdb_backend.security;

import com.auth0.jwt.algorithms.Algorithm;

public class CommonHashAlgorithmProvider {
    private static final String SECRET_STRING = "topsecretstringthatnooneshouldknow";
    private static Algorithm algorithm;

    public static Algorithm currentAlgorithm() {
        if(algorithm == null) {
            algorithm = Algorithm.HMAC256(SECRET_STRING.getBytes());
        }

        return algorithm;
    }
}
