package org.mgw_demo.order_service_interceptor;

import org.json.JSONObject;
import org.mgw_demo.order_service_client.OrderClient;
import org.wso2.micro.gateway.interceptor.*;

/**
 * Hello world!
 *
 */
public class OrderInterceptor implements Interceptor {
    public boolean interceptRequest(Caller caller, Request request) {
        try {
            JSONObject payload = request.getJsonPayload();
            OrderClient orderClient = new OrderClient("localhost:50001");
            JSONObject responsePayload = orderClient.order(payload);

            Response response = new Response();
            response.setResponseCode(200);
            response.setJsonPayload(responsePayload);
            caller.respond(response);

        } catch (InterceptorException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean interceptResponse(Caller caller, Response response) {
        return true;
    }
}
