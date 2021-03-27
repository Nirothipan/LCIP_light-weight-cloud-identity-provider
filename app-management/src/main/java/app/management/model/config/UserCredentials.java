package app.management.model.config;

/**
 * This class represents a credential composed of a username and a password.
 *
 * @since 1.0.0
 */
public class UserCredentials {

    private String username;
    private String password;

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

}
