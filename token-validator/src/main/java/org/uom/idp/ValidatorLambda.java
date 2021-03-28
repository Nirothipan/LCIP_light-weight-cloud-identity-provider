package org.uom.idp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import org.uom.idp.model.TokenData;
import org.uom.idp.service.LicenseValidator;

/**
 * Hello world!
 */
public class ValidatorLambda implements RequestHandler<TokenData, Object> {
    private LicenseValidator licenseValidator;

    public static void main(String[] args) {
        LicenseValidator licenseValidator1 = new LicenseValidator();

        JSONObject response = null;
        try {
            response = licenseValidator1.premain(
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzM4NCJ9.eyJPcmdhbml6YXRpb24gTmFtZSI6IjEyMXF3IiwiaXNzIjoibGNpcCIsInNjb3BlcyI6WyJbJ3Jvb2tpZTIzJywgJ2NvbnN1bWVyMTInXSJdLCJleHAiOjE2MzU3MDUwMDAsImlhdCI6MTYxNjkzODcxNSwiQXBwbGljYXRpb24gTmFtZSI6ImlkLTEyIn0.q7I4St7lXfba3q5bUh-zTn9oJCwokZWxppHdSeNTM3E-LT97ur7AFnoaHqLTFJkwc44e82e9bB-mh4bFv1RoF__bzC_VPKeYD3kEA7IY_jrAPlUOkDAsa9xcEvsnq5Jos8mi1v8nFrgnXhH6XdJSx5j1uQ3M_vsFa__E3PhTUv9GNAulM9JTpwEQXpiq3qdU9QHr9pIHrW4JoUuHQnvEYZd8Mr6a06Q0efQ8H1SReZtleQrpHayFhdv9YJoJtKLgSV5B-eMgB6F2tmfhlFVU14621HLDJvfhxO1MlD4X6ppijYFoDLxUfAxUIynKpMo3v0yDc-Sn_WtGMhyGEvrayg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }

    @Override
    public Object handleRequest(TokenData token, Context context) {

        if (licenseValidator == null) {
            licenseValidator = new LicenseValidator();
        }

        try {
            return licenseValidator.premain(token.getToken());
        } catch (Exception e) {
            e.printStackTrace();
            return getErrorOutput(e.getMessage());
        }
    }

    protected static JSONObject getErrorOutput(String message) {
        JSONObject output = new JSONObject();
        output.put("Status", "Internal Server Error");
        output.put("Message", message);
        return output;
    }

}
