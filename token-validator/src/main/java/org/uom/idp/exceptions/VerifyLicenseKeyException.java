package org.uom.idp.exceptions;


/**
 * Thrown if JWT verification fails.
 *
 * @since 1.0.0
 */
public class VerifyLicenseKeyException extends Exception {
    public VerifyLicenseKeyException(String errMsg, Throwable e) {
        super(errMsg, e);
    }

    public VerifyLicenseKeyException(String errMsg) {
        super(errMsg);
    }
}
