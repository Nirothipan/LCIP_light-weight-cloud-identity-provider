package licensekey.generator.service;

import licensekey.generator.exception.PrivateKeyGenerationException;
import licensekey.generator.utils.Constants;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;

/**
 * This class loads the private key from a file and returns.
 *
 * @since 1.0.0
 */
public class PrivateKeyReader {

    /**
     * @return Private key
     * @throws PrivateKeyGenerationException which capture no file found user.management.exception, invalid algorithm user.management.exception and
     *                                       invalid key spec user.management.exception
     */
    public static PrivateKey getPrivateKey(String keyFileLocation) throws Exception {
        RSAPublicKey publicKey = null;
        String alias = "wso2carbon";
        String password = "wso2carbon";

        // point your keystore here
        InputStream file = PrivateKeyReader.class.getClassLoader().getResourceAsStream("wso2carbon.jks");
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(file, password.toCharArray());

        // Get certificate of public key
        Certificate cert = keystore.getCertificate(alias);
        return (PrivateKey)keystore.getKey(alias, password.toCharArray());

        //        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
//                .withRegion(region)
//                .build();

//        String secret, decodedBinarySecret;
//        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
//                .withSecretId(secretName);
//        GetSecretValueResult getSecretValueResult = null;
//
//        try {
//            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
//        } catch (DecryptionFailureException e) {
//            // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (InternalServiceErrorException e) {
//            // An error occurred on the server side.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (InvalidParameterException e) {
//            // You provided an invalid value for a parameter.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (InvalidRequestException e) {
//            // You provided a parameter value that is not valid for the current state of the resource.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        } catch (ResourceNotFoundException e) {
//            // We can't find the resource that you asked for.
//            // Deal with the exception here, and/or rethrow at your discretion.
//            throw e;
//        }
//
//        // Decrypts secret using the associated KMS CMK.
//        // Depending on whether the secret is a string or binary, one of these fields will be populated.
//        if (getSecretValueResult.getSecretString() != null) {
//            secret = getSecretValueResult.getSecretString();
//        }
//        else {
//            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
//            System.out.println(decodedBinarySecret);
//        }

//        String privateKey = "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAABlwAAAAdzc2gtcn" +
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
//         byte[] keyBytes;
//        keyBytes = privateKey.getBytes();
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//         KeyFactory keyFactory;
//         try {
//             keyFactory = KeyFactory.getInstance("RSA");
//         } catch (NoSuchAlgorithmException e) {
//             throw new PrivateKeyGenerationException("Algorithm Invalid", e);
//         }
//         try {
//             return keyFactory.generatePrivate(spec);
//         } catch (InvalidKeySpecException e) {
//             throw new PrivateKeyGenerationException("Invalid Key spec", e);
//         }
    }
}
