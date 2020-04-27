package org.mgw_demo.order_service_client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.json.JSONObject;
import org.mgw_demo.order_service_lib.OrderServiceGrpc;
import org.mgw_demo.order_service_lib.OrderServiceOuterClass;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

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

    public JSONObject order(JSONObject jsonObject){

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
                    .setDescription("Internal Error")
                    .build();
            return responseToJsonObject(response);
        }
    }

    public void notifyMe (String locationString) {

        OrderServiceOuterClass.StoreLocation.Builder builder = OrderServiceOuterClass.StoreLocation.newBuilder();
        if (OrderServiceOuterClass.Location.CITY_A.name().equals(locationString)) {
            builder.setLocation(OrderServiceOuterClass.Location.CITY_A);
        } else if (OrderServiceOuterClass.Location.CITY_B.name().equals(locationString)) {
            builder.setLocation(OrderServiceOuterClass.Location.CITY_B);
        } else if (OrderServiceOuterClass.Location.CITY_C.name().equals(locationString)) {
            builder.setLocation(OrderServiceOuterClass.Location.CITY_C);
        } else {
            System.out.println("Store is not available");
            return;
        }
        OrderServiceOuterClass.StoreLocation request = builder.build();

        Iterator<OrderServiceOuterClass.ItemDetails> responses;
        try {
            responses = blockingStub.notifyMe(builder.build());
            while (responses.hasNext()) {
                OrderServiceOuterClass.ItemDetails response = responses.next();
                List<OrderServiceOuterClass.Item> itemList = response.getItemList();

                for (OrderServiceOuterClass.Item item : itemList) {
                    System.out.println(item.getItemName() + " : " + item.getQuantity());
                }
                System.out.println("\n");
            }
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: {0} : " +  e.getStatus());
        }
        System.out.println("Stream is closed. ");
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
        JSONObject jsonObject = orderClient.order(new JSONObject(json));
        System.out.println(jsonObject.toString());

        System.out.println(" ----- \n\n");

        orderClient.notifyMe("CITY_A");

    }

    public static void invokeNotifyFunction () {

    }
}
