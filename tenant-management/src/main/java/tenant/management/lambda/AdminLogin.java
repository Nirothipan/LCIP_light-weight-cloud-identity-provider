package tenant.management.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import tenant.management.lambda.dao.AdminLoginData;

public class AdminLogin implements RequestHandler<AdminLoginData, Object> {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = "cloud-idp-admin-cred";

    public static void main(String[] args) {
        AdminLoginData adminLoginData = new AdminLoginData();
        adminLoginData.setUserName("lcipadmin");
        adminLoginData.setPassword("adminlcip");
        JSONObject status = new JSONObject();
        if ("lcipadmin".equals(adminLoginData.getUserName()) && "adminlcip".equals(adminLoginData.getPassword())) {
            status.put("status", true);
        } else {
            status.put("status", false);
        }
        System.out.println(status.toString());
    }

    @Override
    public Object handleRequest(AdminLoginData adminLoginData, Context context) {
        JSONObject status = new JSONObject();
        if ("lcipadmin".equals(adminLoginData.getUserName()) && "adminlcip".equals(adminLoginData.getPassword())) {
            status.put("status", true);
        } else {
            status.put("status", false);
        }
        System.out.println(status.toString());
        return status;
    }

    private static void retrieveItem(AdminLoginData adminLoginData) {
        Table table = dynamoDB.getTable(tableName);

        try {
            Item item = table.getItem("username", adminLoginData.getUserName());

            System.out.println("Printing item after retrieving it....");
            System.out.println(item.toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }

    }
}
