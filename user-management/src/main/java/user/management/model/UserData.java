package user.management.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains the information related user/customer doing the JWT generation.
 *
 * @since 1.0.0
 */
public class UserData {

    @SerializedName("username")
    private String username;

    @SerializedName("user_email")
    private String useremail;

    @SerializedName("password")
    private String password;

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
