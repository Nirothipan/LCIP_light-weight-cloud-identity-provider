package org.uom.idp.exceptions;

/**
 * Thrown if unable to decode the JWT.
 *
 * @since 1.0.0
 */
public class DecodeLicenseKeyException extends Exception {
    public DecodeLicenseKeyException(String errMsg, Throwable e) {
        super(errMsg, e);
    }

    public DecodeLicenseKeyException(String errMsg) {
        super(errMsg);
    }
}
