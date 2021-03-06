package licensekey.generator.model.entity;

import licensekey.generator.utils.Constants;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

/**
 * Database Entity.
 *
 * @since 1.0.0
 */
@NamedNativeQueries({
        @NamedNativeQuery(name = Constants.Database.Queries.FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER_NAME,
                query = Constants.Database.Queries.FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER,
                resultClass = LicensekeyGeneratorEntity.class) })

@Entity
@Table(name = "TOKEN_INFO")
public class LicensekeyGeneratorEntity implements Serializable {

    private static final long serialVersionUID = -4997964964871690908L;

    @Column(name = "token")
    private String token;

    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "created_date")
    private long createdDate;

    @Id
    @Column(name = "user_name")
    private String userName;

    @Id
    @Column(name = "app_id")
    private String appId;

    @Column(name = "expiry_Date")
    private long expiryDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}
