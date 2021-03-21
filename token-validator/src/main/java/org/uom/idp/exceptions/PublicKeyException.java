package org.uom.idp.exceptions;

/**
 * Thrown if the Public key is invalid.
 *
 * @since 1.0.0
 */
public class PublicKeyException extends Exception {
    public PublicKeyException(String errMsg, Throwable e) {
        super(errMsg, e);
    }
}
