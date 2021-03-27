package app.management.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "APPLICATION_INFO")
public class ApplicationDataEntity implements Serializable {

    private static final long serialVersionUID = -4997964964871690908L;

    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    @Id
    @Column(name = "application_name")
    private String appName;

    @Column(name = "callBackUrl")
    private String callBackUrl;

    @Column(name = "client_id")
    private String clientId;

    public ApplicationDataEntity() {
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getId() {
        return tenantId;
    }

    public void setId(String id) {
        this.tenantId = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
