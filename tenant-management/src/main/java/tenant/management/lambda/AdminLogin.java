package tenant.management.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import tenant.management.lambda.dao.AdminLoginData;

public class AdminLogin implements RequestHandler<AdminLoginData, Object> {

    @Override
    public Object handleRequest(AdminLoginData adminLoginData, Context context) {
        return null;
    }
}
