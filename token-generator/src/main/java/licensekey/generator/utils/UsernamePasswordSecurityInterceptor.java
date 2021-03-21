package licensekey.generator.utils;

import licensekey.generator.model.config.Configuration;
import org.wso2.msf4j.security.basic.AbstractBasicAuthSecurityInterceptor;

/**
 * This class validates the user.
 *
 * @since 1.0.0
 */
public class UsernamePasswordSecurityInterceptor extends AbstractBasicAuthSecurityInterceptor {

    private Configuration configFile;

    /**
     * Constructor to create a Username Password Security Interceptor object.
     *
     * @param configFile Configuration file
     */
    public UsernamePasswordSecurityInterceptor(Configuration configFile) {

        this.configFile = configFile;
    }

    @Override
    protected boolean authenticate(String username, String password) {

        return username.equals(configFile.getMicroServiceConnection().getCredentials().getUsername()) &&
                password.equals(configFile.getMicroServiceConnection().getCredentials().getPassword());
    }
}
