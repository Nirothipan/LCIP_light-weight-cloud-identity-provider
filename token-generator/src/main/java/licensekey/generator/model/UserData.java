package licensekey.generator.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class contains the information related user/customer doing the JWT generation.
 *
 * @since 1.0.0
 */
public class UserData {

    @SerializedName("username")
    private String username;
    @SerializedName("creator_username")
    private String creatorUsername;
    @SerializedName("expiry-date")
    private String expiryDate;
    @SerializedName("apis")
    private List<String> apis;

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getExpiryDate() {

        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {

        this.expiryDate = expiryDate;
    }

    public List<String> getApis() {

        return apis;
    }

    public void setApis(List<String> apis) {

        this.apis = apis;
    }

    public String getCreatorUsername() {

        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {

        this.creatorUsername = creatorUsername;
    }
}
