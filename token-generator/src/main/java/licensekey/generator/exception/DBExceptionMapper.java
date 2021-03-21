
package licensekey.generator.exception;

import licensekey.generator.Application;
import licensekey.generator.model.Error;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * This is the mapper for {@link DBException}.
 *
 * @since 1.0.0
 */
public class DBExceptionMapper implements ExceptionMapper<PrivateKeyGenerationException> {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Override
    public Response toResponse(PrivateKeyGenerationException e) {

        LOG.error(e.getMessage(), e);
        Error error = new Error();
        error.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        error.setMessage("Internal server error occurred. Please try again.");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", error);
        jsonObject.put("errorCode", 1);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(jsonObject)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
