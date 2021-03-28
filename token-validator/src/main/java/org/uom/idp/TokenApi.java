package org.uom.idp;

import org.uom.idp.service.LicenseValidator;

public class TokenApi {

    LicenseValidator licenseValidator = new LicenseValidator();

    public boolean validate(String token) {
        try {
            return licenseValidator.validateToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}