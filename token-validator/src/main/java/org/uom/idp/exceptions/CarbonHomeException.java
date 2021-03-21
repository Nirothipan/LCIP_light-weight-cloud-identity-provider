package org.uom.idp.exceptions;

/**
 * Thrown if carbon.home property is not set.
 *
 * @since 1.0.0
 */
public class CarbonHomeException extends Exception {
    public CarbonHomeException(String errMsg) {
        super(errMsg);
    }
}
