package licensekey.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import licensekey.generator.exception.DBExceptionMapper;
import licensekey.generator.exception.PrivateKeyGenerationExceptionMapper;
import licensekey.generator.exception.SalesForceExceptionMapper;
import licensekey.generator.model.config.Configuration;
import licensekey.generator.service.KeyGeneratorService;
import licensekey.generator.utils.Constants;
import licensekey.generator.utils.UsernamePasswordSecurityInterceptor;
import licensekey.generator.utils.Utils;
import org.wso2.msf4j.MicroservicesRunner;

/**
 * Main Application class.
 *
 * @since 1.0.0
 */
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    /**
     * Main method.
     *
     * @param args args Arguments for the main method.
     */
    public static void main(String[] args) {

        LOG.info("Key generator micro-service starting...");
        Configuration config = Utils.loadConfig(Constants.Configurations.CONFIGURATION_YAML, Configuration.class);
        MicroservicesRunner microservicesRunner = new MicroservicesRunner();
                //.addGlobalRequestInterceptor(new UsernamePasswordSecurityInterceptor(config));
        microservicesRunner.deploy(new KeyGeneratorService(config))
                .addExceptionMapper(
                        new PrivateKeyGenerationExceptionMapper(),
                        new DBExceptionMapper(),
                        new SalesForceExceptionMapper()
                )
                .start();
    }
}
