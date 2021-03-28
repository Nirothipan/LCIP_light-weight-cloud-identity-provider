package tenant.management.model.entity;

import tenant.management.utils.Constants;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

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

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "admin_username")
    private String adminName;

    public TenantDataEntity() {
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
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
