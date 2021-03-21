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
     * @throws PrivateKeyGenerationException which capture no file found exception, invalid algorithm exception and
     *                                       invalid key spec exception
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
        
        // byte[] keyBytes;
        // try {
        //     keyBytes = Files.readAllBytes(Paths.get(keyFileLocation));
        // } catch (IOException e) {
        //     throw new PrivateKeyGenerationException("Private key file not found", e);
        // }
        // PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        // KeyFactory keyFactory;
        // try {
        //     keyFactory = KeyFactory.getInstance("RSA");
        // } catch (NoSuchAlgorithmException e) {
        //     throw new PrivateKeyGenerationException("Algorithm Invalid", e);
        // }
        // try {
        //     return keyFactory.generatePrivate(spec);
        // } catch (InvalidKeySpecException e) {
        //     throw new PrivateKeyGenerationException("Invalid Key spec", e);
        // }
    }
}
