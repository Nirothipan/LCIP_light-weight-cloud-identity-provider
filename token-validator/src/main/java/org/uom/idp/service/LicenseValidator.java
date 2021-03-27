package org.uom.idp.service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import org.uom.idp.exceptions.DecodeLicenseKeyException;
import org.uom.idp.exceptions.PrivateKeyGenerationException;
import org.uom.idp.exceptions.PublicKeyException;
import org.uom.idp.exceptions.VerifyLicenseKeyException;
import org.uom.idp.utils.Constants;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

//        String publicKey = "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAABlwAAAAdzc2gtcn" +
//                "NhAAAAAwEAAQAAAYEAv7PeGN/20EXvpnw2A0zkaUNkxNYb7J59vVs6NlHMlMeTRzJNeOoP" +
//                "r1p9nsnqJPp/DQ0E4kS+1rk6E4TRM3dBYf18/FGds2LwDio2XaFEq1JtklykFIteludCKM" +
//                "wYNcEF35x585y0GZ6wL0EztIMWMOgc2UmnIKoQWLtRF1aMKp2pIib90Jv1IXtBJakuywPH" +
//                "K0wjGl46ti2v+AfHVS8SBJj6gw/Ehz9P17zo2fAL3i0AVp4yV1KzqVcNXPlyZxU7oxYO+F" +
//                "goNMD2ejT/cjmcGxGHlg4+Qs3K3hCS5N9OrqWtIkJBNELiwUFta2RBlUxL5+bv/4ufN4CO" +
//                "zFDycp3gbJ4T+cJBqZCkY3/ugW5rM+bVxgUoyIH4QMhJ56PkJkvPLgm8z8KFpcR+wErywJ" +
//                "Y7rCfIFmKNhwBEP0urU6+XNbyy5dESZVOCRB3lw0PSxl7VYaFBz6Qfewe19dl00w6HzqZI" +
//                "hYHytzCT8Wcb+Hvb/TiRoK4EjudMDssqDIfeYTqnAAAFiIIkOduCJDnbAAAAB3NzaC1yc2" +
//                "EAAAGBAL+z3hjf9tBF76Z8NgNM5GlDZMTWG+yefb1bOjZRzJTHk0cyTXjqD69afZ7J6iT6" +
//                "fw0NBOJEvta5OhOE0TN3QWH9fPxRnbNi8A4qNl2hRKtSbZJcpBSLXpbnQijMGDXBBd+cef" +
//                "OctBmesC9BM7SDFjDoHNlJpyCqEFi7URdWjCqdqSIm/dCb9SF7QSWpLssDxytMIxpeOrYt" +
//                "r/gHx1UvEgSY+oMPxIc/T9e86NnwC94tAFaeMldSs6lXDVz5cmcVO6MWDvhYKDTA9no0/3" +
//                "I5nBsRh5YOPkLNyt4QkuTfTq6lrSJCQTRC4sFBbWtkQZVMS+fm7/+LnzeAjsxQ8nKd4Gye" +
//                "E/nCQamQpGN/7oFuazPm1cYFKMiB+EDISeej5CZLzy4JvM/ChaXEfsBK8sCWO6wnyBZijY" +
//                "cARD9Lq1OvlzW8suXREmVTgkQd5cND0sZe1WGhQc+kH3sHtfXZdNMOh86mSIWB8rcwk/Fn" +
//                "G/h72/04kaCuBI7nTA7LKgyH3mE6pwAAAAMBAAEAAAGBAKjgxXlYY8/arGIX7mU96cIpkq" +
//                "4siHEMtmyH32JW8UjoAACqecFZQPQTXbLy4Ygt/Jr8qx7BvPTgPB9R1BHorvqqGt8U7z5G" +
//                "nH9+BGK7XQkoFkrnvcwzcuKxW4EhOvqvzoLNEEnh9G7VcxRN9kTBbl0rPa7VyZtm9fuVGL" +
//                "zci6JHPeZOPy71FR+nux54SjaiX2TvL8Vc2Y//85MnFx0d2FuggJ/Q1irnRWkijGbhpzLU" +
//                "pgWoW2S+AHPfsnADeBsDgfuyxrM/agx1X+LPrrojwwiaoh0UyA6mTNbAee1DuzrOIx/1Qj" +
//                "GRAlGHNBZsvSqMyXxeybekUt8jShi9XNLgcnXsSxdNRXXJluYGFo/dTJaBjre2HBtlYT1M" +
//                "GfbyqaHd2YrEnj7xeJfKFfdiZHXqaQZol8rs6RFjDSHnGmjqMoa8hkX/gsuqhFKUwRG4Ok" +
//                "62lv3KPf97V+ceP9BAplXn7GreFqTjkKai0PpquUyHbwsq1Z83K76dFRMfNXdP4JuXUQAA" +
//                "AMEA8v6ioo9j7ZH7atIf4V9XThLBKcAMG+lii1EA/8H+RyZt5TjTf50pcvZMN0xJN2lw74" +
//                "InJ38rb7x1TpcR0dhEsJYf2o83y8DrBvqHYiyme9Rlb+HPH5cb/sJkRO1FazB14xvTVQXx" +
//                "nZD/DHQtMSwntQfHLcN0B9wC/wtetzjPu+3g8MF/u+utBOjgyKp4kwQYw780DjBq3MRHXW" +
//                "t+IOKlW62fZofvbt5Ej0DWZRgZpIP+WxoPAHcQRtKF8NRA8FGKAAAAwQD1UdXybFi0EjcR" +
//                "6mbUTCa5ffC7jwFx++XB46TwVbiNW4dSm/6ajK0e+m6729OZ1tl3QpSTZcQP+1qHtevcrv" +
//                "r0oX9VsXAuUuIws6De/uzD8bVT3KKRjDVHGjyekUPCl/2cP3EU3Aonky7UrlNdhdhIgBuA" +
//                "qqNB15aCHzmR/Ve7HlyZhzmIVLIZvukhmX3yTUMtAoMl0pEiwQgK7IXmuKfgcgZyqKQkEr" +
//                "99dODbl1SWVF16dKXWD+F6fH0XS+joSfMAAADBAMgMc/PdZxspTjQIXHbcWLDTItKOU4ej" +
//                "MChPcRdfnFyu5DQN3gojYNZxSg8DiB7Ddf/6fDlnTTGVyEdyIJPVq0qRpsvGsaqRBQXAdi" +
//                "dfDxsomf9dKH9D41445YiDdG1advWzBWFQfuABqgPtToQV5m6Qi3tVDUNSmLwU7ufIYreg" +
//                "oScUBatamYOyPBc1V1fCHyfslRlbijydhadP1p8nYsl4NXeuFcgNs0MuVRUaZYgIWykaTy" +
//                "Hxw+rQ4HQfyOclfQAAAA1hcnVuYW5AQXJ1bmFuAQIDBA==";
//
//        byte[] keyBytes;
//        keyBytes = publicKey.getBytes();
//        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory;
//        try {
//            keyFactory = KeyFactory.getInstance("RSA");
//        } catch (NoSuchAlgorithmException e) {
//            throw new PrivateKeyGenerationException("Algorithm Invalid", e);
//        }
//        try {
//            return keyFactory.generatePublic(publicKeySpec);
//        } catch (InvalidKeySpecException e) {
//            throw new PrivateKeyGenerationException("Invalid Key spec", e);
//        }

    }

    public static void getSecret() {

        String secretName = "dev/lcip-secret-keys";
        String endpoint = "secretsmanager.us-east-1.amazonaws.com";
        String region = "us-east-1";

        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        AWSSecretsManager client = clientBuilder.build();

        String secret;
        ByteBuffer binarySecretData;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName).withVersionStage("AWSCURRENT");
        GetSecretValueResult getSecretValueResult = null;
        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        } catch (ResourceNotFoundException e) {
            System.out.println("The requested secret " + secretName + " was not found");
        } catch (InvalidRequestException e) {
            System.out.println("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("The request had invalid params: " + e.getMessage());
        }

        if (getSecretValueResult == null) {
            return;
        }

        // Depending on whether the secret was a string or binary, one of these fields will be populated
        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
            System.out.println(secret);
        } else {
            binarySecretData = getSecretValueResult.getSecretBinary();
            System.out.println(binarySecretData.toString());
        }
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
