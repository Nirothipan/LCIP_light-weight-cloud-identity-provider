package app.management.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains the information related application.
 *
 * @since 1.0.0
 */
public class ApplicationData {

    @SerializedName("appName")
    private String appName;
    @SerializedName("callBackUrl")
    private String callBackUrl;

    public String getAppName() {

        return appName;
    }

    public void setAppName(String appName) {

        this.appName = appName;
    }

    public String getCallBackUrl() {

        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {

        this.callBackUrl = callBackUrl;
    }
}