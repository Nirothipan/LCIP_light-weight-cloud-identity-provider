package licensekey.generator.model.entity;

import licensekey.generator.utils.Constants;

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
@NamedNativeQueries({
        @NamedNativeQuery(
                name = Constants.Database.Queries.FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER_NAME,
                query = Constants.Database.Queries.FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_USER,
                resultClass = LicensekeyGeneratorEntity.class
        )
})

@Entity
@Table(name = "KEY_GEN_INFO")
public class LicensekeyGeneratorEntity implements Serializable {

    private static final long serialVersionUID = -4997964964871690908L;

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "creator_username")
    private String creatorUsername;

    @Column(name = "jwt")
    private String jwtToken;

    @Column(name = "expiry_date")
    private long expiryDate;

    @Column(name = "created_date")
    private long createdDate;

    public static long getSerialVersionUID() {

        return serialVersionUID;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getCreatorUsername() {

        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {

        this.creatorUsername = creatorUsername;
    }

    public String getJwtToken() {

        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {

        this.jwtToken = jwtToken;
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
