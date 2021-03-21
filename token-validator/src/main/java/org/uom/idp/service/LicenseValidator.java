package org.uom.idp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import org.uom.idp.exceptions.DecodeLicenseKeyException;
import org.uom.idp.exceptions.PublicKeyException;
import org.uom.idp.exceptions.VerifyLicenseKeyException;
import org.uom.idp.utils.Constants;

import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Logger;
import java.security.KeyStore;
import java.security.cert.Certificate;

import static org.uom.idp.utils.Constants.*;

/**
 * This class validates the license key.
 * <p>
 * This class holds the "premain" method of the Java agent. This method reads the license
 * key file located at {Carbon Home}/{@link Constants#LICENSE_KEY_PATH} and validate against,
 * 1. Issuer
 * 2. Expire date
 * 3. {@link Constants#API_CODES_CLAIM}
 * 4. Signature
 *
 * @since 1.0.0
 */
public class LicenseValidator {

    private static final Logger logger = Logger.getLogger(LicenseValidator.class.getName());

    /**
     * After the Java Virtual Machine (JVM) has initialized,  premain method will be called. This method will load
     * the license key and validate followings,
     * <p>
     * 1. Issuer
     * 2. Expire date
     * 3. {@link Constants#API_CODES_CLAIM}
     * 4. Signature
     *
     * @param agentArgument Argument passed for the Java agent
     */
    public JsonObject premain(final String agentArgument) throws Exception {
        DecodedJWT decodedJWT = decodeLicenseKey(agentArgument);
        return createOutput(verifyLicenseKey(decodedJWT));
    }

    /**
     * This method create the output Object
     *
     * @param statusMsg       Status Message
     * @return JsonObject
     */
    private JsonObject createOutput(String statusMsg) {

        JsonObject output = new JsonObject();
        output.addProperty("status", statusMsg);
        return output;
    }



    /**
     * Returns an Input stream for the Public cert file in the resources/{@link Constants#PUBLIC_KEY}.
     *
     * @return {@link InputStream}
     */
    private static InputStream getPublicKeyFileStream() {
        return LicenseValidator.class.getClassLoader().getResourceAsStream(PUBLIC_KEY);

    }

    /**
     * Load public certificate in .pem format as a {@link RSAPublicKey}.
     *
     * @return public key {@link RSAPublicKey}
     * @throws PublicKeyException If cannot construct the public certificate
     */
    private static RSAPublicKey getRSAPublicKey() throws Exception {
        RSAPublicKey publicKey = null;
        String alias = "wso2carbon";
        String password = "wso2carbon";

        InputStream file = LicenseValidator.class.getClassLoader().getResourceAsStream("wso2carbon.jks");
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(file, password.toCharArray());

        // Get certificate of public key
        Certificate cert = keystore.getCertificate(alias);
        return (RSAPublicKey) cert.getPublicKey();

    }

    /**
     * This method reads the license key from the given file and construct a JWT if the following claims are
     * present,
     * 1. Issuer
     * 2. Expire date
     * 3. {@link Constants#API_CODES_CLAIM}
     * @param licenseKey license key
     * @return Decoded JWT token {@link DecodedJWT}
     * @throws DecodeLicenseKeyException If the JWT is not valid
     */
    private static DecodedJWT decodeLicenseKey(String licenseKey) throws DecodeLicenseKeyException {

        DecodedJWT decodedJWT = JWT.decode(licenseKey);
        if (decodedJWT.getIssuer() == null) {
            throw new DecodeLicenseKeyException("Issuer claim is not defined");
        }
        if (decodedJWT.getExpiresAt() == null) {
            throw new DecodeLicenseKeyException("Expire data is not defined");
        }
        String[] jwtProductCodes = decodedJWT.getClaim(API_CODES_CLAIM).asArray(String.class);
        if (jwtProductCodes == null || jwtProductCodes.length == 0) {
            throw new DecodeLicenseKeyException(String.format("%s claim is not configured or empty",
                    API_CODES_CLAIM));
        }
        return decodedJWT;
    }
    /**
     * Verifies following JWT claims.
     * <p>
     * 1. Signature
     * 2. Expire date
     * 3. The Product code claim is valid if the product code or "wso2carbon" is with in
     * the jwt claim {@link Constants#API_CODES_CLAIM}.
     * 4. Issuer
     * <p>
     * Assumption: Decoded JWT has {@link Constants#API_CODES_CLAIM} & "exp" claims
     *
     * @param decodedJWT Decode JWT
     * @throws PublicKeyException        If cannot construct the public certificate
     * @throws VerifyLicenseKeyException If the token is invalid
     */
    private static String verifyLicenseKey(final DecodedJWT decodedJWT)
            throws Exception, VerifyLicenseKeyException {
        String status = "failed";
        Algorithm algorithm = Algorithm.RSA384(getRSAPublicKey(), null);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(Constants.ISSUER)
                .build();
        // Verify Expire date + signature
        try {
            verifier.verify(decodedJWT);
            status = "success";
        } catch (TokenExpiredException e) {
            status = "License key has expired";
        } catch (InvalidClaimException e) {
            status = "Issuer is invalid";
        } catch (JWTVerificationException e) {
            status = "Signature is invalid";
        }
        return status;
    }

}
