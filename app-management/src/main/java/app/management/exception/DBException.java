package app.management.exception;

/**
 * This class wraps the actual user.management.exception with a custom message.
 *
 * @since 1.0.0
 */
public class DBException extends Exception {

    public DBException(String msg) {

        super(msg);
    }

    public DBException(Throwable t) {

        super(t);
    }

    public DBException(String msg, Throwable t) {

        super(msg, t);
    }

}
