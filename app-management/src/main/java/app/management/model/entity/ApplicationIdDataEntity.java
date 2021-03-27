package app.management.model.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "APPLICATION_INFO")
public class ApplicationIdDataEntity implements Serializable {

    private static final long serialVersionUID = -4997964964871690908L;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "application_name")
    private String appName;


    @Column(name = "callBackUrl")
    private String callBackUrl;

    @Id
    @Column(name = "client_id")
    private String clientId;

    public ApplicationIdDataEntity() {
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
