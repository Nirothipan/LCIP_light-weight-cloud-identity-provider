package licensekey.generator.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class contains the information related user/customer doing the JWT generation.
 *
 * @since 1.0.0
 */
public class UserData {

    private String username;

    private long expiryDate;

    private String appId;

    private String tenantId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
