package user.management.model.entity;

import user.management.utils.Constants;

import java.io.Serializable;
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
@NamedNativeQueries({ @NamedNativeQuery(name = Constants.Database.Queries.GET_ALL_USER,
        query = Constants.Database.Queries.GET_ALL_USERS_QUERY,
        resultClass = UserDataEntity.class) })

@Entity
@Table(name = "USER_INFO")
public class UserDataEntity implements Serializable {

    @Column(name = "id")
    private int id;

    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    @Id
    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "password")
    private String password;

    public UserDataEntity() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String id) {
        this.tenantId = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
