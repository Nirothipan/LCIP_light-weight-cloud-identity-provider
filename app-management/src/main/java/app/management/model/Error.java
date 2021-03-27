package app.management.model;

/**
 * Json Object when there is an error.
 *
 * @since 1.0.0
 */
public class Error {

    private int errorCode;
    private String message;

    public int getCode() {

        return errorCode;
    }

    public void setCode(int errorCode) {

        this.errorCode = errorCode;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

}
