package org.uom.idp.exceptions;

/**
 * Thrown if unable to read the given file or get product code.
 *
 * @since 1.0.0
 */
public class ProductCodeException extends Exception {

    public ProductCodeException(String errMsg) {
        super(errMsg);
    }

    public ProductCodeException(String errMsg, Throwable e) {
        super(errMsg, e);
    }
}
