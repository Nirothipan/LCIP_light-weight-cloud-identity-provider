package licensekey.generator.service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import licensekey.generator.exception.PrivateKeyGenerationException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;

/**
 * This class loads the private key from a file and returns.
 *
 * @since 1.0.0
 */
public class PrivateKeyReader {

    private static boolean fileDownloaded = false;

    /**
     * @return Private key
     * @throws PrivateKeyGenerationException which capture no file found user.management.exception, invalid algorithm user.management.exception and
     *                                       invalid key spec user.management.exception
     */
    public static PrivateKey getPrivateKey() throws Exception {
        retrieveCredentials();
        RSAPublicKey publicKey = null;
        JSONObject credentials = retrieveCredentials();
        String alias = credentials.get("lcip-jks-alias").toString();
        String password = credentials.get("lcip-jks-password").toString();

        if (!fileDownloaded) {
            downloadFileFromS3();
            fileDownloaded = true;
        }

        String bucket_name = "cloud-idp-bucket";
        String key_name = "wso2carbon.jks";

        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        S3Object object = s3Client.getObject(new GetObjectRequest(bucket_name, key_name));
        InputStream file = object.getObjectContent();
        // Process the objectData stream.


        // point your keystore here
       // InputStream file = PrivateKeyReader.class.getClassLoader().getResourceAsStream("wso2carbon.jks");
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(file, password.toCharArray());

        // Get certificate of public key
        Certificate cert = keystore.getCertificate(alias);
        file.close();
        return (PrivateKey)keystore.getKey(alias, password.toCharArray());

    }

    public static JSONObject retrieveCredentials() throws ParseException {

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
            return null;
        }

        // Depending on whether the secret was a string or binary, one of these fields will be populated
        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
            JSONParser parser = new JSONParser();
            JSONObject secretJson = (JSONObject) parser.parse(secret);
            return secretJson;
        } else {
            binarySecretData = getSecretValueResult.getSecretBinary();
            System.out.println(binarySecretData.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        downloadFileFromS3();
    }

    private static void downloadFileFromS3() {
        String bucket_name = "cloud-idp-bucket";
        String key_name = "wso2carbon.jks";
        System.out.format("Downloading %s from S3 bucket %s...\n", key_name, bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-east-1").build();
        try {
            S3Object o = s3.getObject(bucket_name, key_name);
            S3ObjectInputStream s3is = o.getObjectContent();
            File downloadedFile = new File(key_name);
            FileOutputStream fos = new FileOutputStream(downloadedFile);
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
