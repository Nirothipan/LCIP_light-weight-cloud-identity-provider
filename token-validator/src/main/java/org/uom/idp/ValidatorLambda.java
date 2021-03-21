package org.uom.idp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.uom.idp.model.TokenData;
import org.uom.idp.service.LicenseValidator;

/**
 * Hello world!
 *
 */
public class ValidatorLambda implements RequestHandler<TokenData, Object>
{
    private LicenseValidator licenseValidator;

    public static void main(String[] args) {
        LicenseValidator licenseValidator1 = new LicenseValidator();

        JsonObject response = null;
        try {
            response = licenseValidator1.premain("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhcGlDb2RlcyI6WyJhcGkxIiwiYXBpMiJdLCJpc3MiOiJ1b20ubGsiLCJleHAiOjE2MDUwMzMwMDAsImlhdCI6MTYxMzg4ODE5MH0.TndbmCS6YKSbTlanwKr1PwweIu4Sl9SY_bmbKM2UwZ679i_2WWkMbYrmK5ICaxDAy00p10nYHVbxspGb0DGib6CyNEiCkr4d3dbplZFDeuJFiDoC5GRg9cqpQjPXuC5-nC9gX1rkGIIrMWDsWaowXSXWSO0o4lTKqtrnh8cYamUuedoRB92AgbsWizjoaZy_mF-aEc6baqlF0bBpGiKD95zfC4QShI-5DIDLABBVXv_eDkg6tO3T6Ddnr_UU5ZDIikLgXx7wHz_Y3cq9tIIuyDfddCwqqxtvRmgdIR4FMOhRRGLnvoRMczDlgcSyQMVLsbKkiML_G2kKrkhDDb_AZaA9uNaobXn4J-Yxy_-tPanGeDbnb7GV717Nqqc4EYQiUbzNlGcXxsGtDSOKFuJL7m9dsH6e4nYUDucB-iURHLKtxW-gF7wFtgXEy5q6WXUy2E8fDbQ9QCJvpVuMgb64cqPeAZJRCJaWfNE4jbXZaYOEe5FIx3Ep020M5pa5vjzN");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response.toString());
    }

    @Override
    public Object handleRequest(TokenData token, Context context) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Invoking Validator Lambda function");
        System.out.println("Printing token : " + token.getToken());

        if (licenseValidator == null) {
            licenseValidator = new LicenseValidator();
        }

        JsonObject response = null;
        try {
            response = licenseValidator.premain(token.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response == null ? "Exception thrown" : response.toString();
    }
}
