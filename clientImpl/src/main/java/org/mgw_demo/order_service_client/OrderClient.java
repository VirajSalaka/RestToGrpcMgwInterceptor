package org.mgw_demo.order_service_client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.json.JSONObject;
import org.mgw_demo.order_service_lib.OrderServiceGrpc;
import org.mgw_demo.order_service_lib.OrderServiceOuterClass;

public class OrderClient {
    private final OrderServiceGrpc.OrderServiceBlockingStub blockingStub;

    public OrderClient(String targetUrl) {
        String modifiedUrl = targetUrl;

        //url should be provided without the transport scheme
        String[] urlElements = targetUrl.split(":");
        if (urlElements.length == 3) {
            modifiedUrl = urlElements[1] + ":" + urlElements[2];
        }
        ManagedChannel channel = ManagedChannelBuilder.forTarget(modifiedUrl).usePlaintext().build();
        blockingStub = OrderServiceGrpc.newBlockingStub(channel);
    }

    public JSONObject createOrder(JSONObject jsonObject){

        String item = jsonObject.getString("item");
        int quantity = jsonObject.getInt("quantity");
        String location = jsonObject.getString("location");

        OrderServiceOuterClass.OrderResponse response = null;
        try {
            OrderServiceOuterClass.OrderRequest request = OrderServiceOuterClass.OrderRequest.newBuilder()
                    .setItem(item)
                    .setQuantity(quantity)
                    .setLocation(location).build();
                response = blockingStub.order(request);
            return responseToJsonObject(response);

        } catch (StatusRuntimeException e) {
            response = OrderServiceOuterClass.OrderResponse.newBuilder()
                    .setPrice(0)
                    .setStatus(OrderServiceOuterClass.Status.FAILED)
                    .setDescription(e.getStatus().getDescription())
                    .build();
            return responseToJsonObject(response);
        }
    }

    private JSONObject responseToJsonObject (OrderServiceOuterClass.OrderResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("price", response.getPrice());
        jsonObject.put("status", response.getStatus().name());
        jsonObject.put("description", response.getDescription());
        return jsonObject;
    }

    //for testing purposes
    public static void main(String[] args) {
        String targetUrl = "localhost:50001";
        String json = "{\"item\":\"Cake\", \"quantity\":3, \"location\":\"Colombo\"}";
        OrderClient orderClient = new OrderClient(targetUrl);
        JSONObject jsonObject = orderClient.createOrder(new JSONObject(json));
        System.out.println(jsonObject.toString());
    }
}
