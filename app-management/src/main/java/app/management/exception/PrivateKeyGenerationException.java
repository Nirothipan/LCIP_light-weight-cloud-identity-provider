package app.management.exception;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * This class wraps the actual user.management.exception with a custom message.
 *
 * @since 1.0.0
 */
public class PrivateKeyGenerationException extends Exception {

    public PrivateKeyGenerationException(String ioException, IOException e) {

        super(ioException, e);
    }

    public PrivateKeyGenerationException(String algorithmNotFound, NoSuchAlgorithmException e) {

        super(algorithmNotFound, e);
    }

    public PrivateKeyGenerationException(String invalidKeySpec, InvalidKeySpecException e) {

        super(invalidKeySpec, e);
    }
}
