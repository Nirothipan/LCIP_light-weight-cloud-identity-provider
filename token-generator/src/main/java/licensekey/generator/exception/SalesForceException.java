package licensekey.generator.exception;

import java.io.IOException;

/**
 * This class wraps the actual exception with a custom message.
 *
 * @since 1.0.0
 */
public class SalesForceException extends Exception {

    public SalesForceException(String malformedURLException, IOException e) {

        super(malformedURLException, e);
    }
}
