package tenant.management.model.entity;

import tenant.management.utils.Constants;

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
        @NamedNativeQuery(name = Constants.Database.Queries.FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_tenant_NAME,
                query = Constants.Database.Queries.FIND_LICENSE_KEY_IF_EXISTS_FOR_A_GIVEN_tenant,
                resultClass = TenantDataEntity.class) })

@Entity
@Table(name = "TENANT_INFO")
public class TenantDataEntity implements Serializable {

    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "admin_username")
    private String adminName;

    public TenantDataEntity() {
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String id) {
        this.tenantId = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
