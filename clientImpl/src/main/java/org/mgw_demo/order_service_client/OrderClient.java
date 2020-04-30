package org.mgw_demo.order_service_client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import org.json.JSONObject;
import org.mgw_demo.order_service_lib.OrderServiceGrpc;
import org.mgw_demo.order_service_lib.OrderServiceOuterClass;

import java.util.Iterator;
import java.util.List;

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

    public OrderClient(String targetUrl, String token) {
        String modifiedUrl = targetUrl;

        //url should be provided without the transport scheme
        String[] urlElements = targetUrl.split(":");
        if (urlElements.length == 3) {
            modifiedUrl = urlElements[1] + ":" + urlElements[2];
        }

        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + token);

        ManagedChannel channel = ManagedChannelBuilder.forTarget(modifiedUrl).usePlaintext().build();
        OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
        blockingStub = MetadataUtils.attachHeaders(stub,metadata);
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
                response = blockingStub.placeOrder(request);
            return responseToJsonObject(response);

        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: {0} : " +  e.getStatus());
            return null;
        }
    }

    public void subscribeItemDetails (String locationString) {

        OrderServiceOuterClass.StoreLocation.Builder builder = OrderServiceOuterClass.StoreLocation.newBuilder();

        //to check the availability of store
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
            responses = blockingStub.subscribeItemDetails(builder.build());
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

    public void getItemDetailsFromStore(String locationString) {


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

        try {
            OrderServiceOuterClass.ItemDetails itemList = blockingStub.getItemDetails(request);
            for (OrderServiceOuterClass.Item item : itemList.getItemList()) {
                System.out.println(item.getItemName() + " : " + item.getQuantity());
            }
        } catch (StatusRuntimeException e) {
            System.out.println("RPC failed: {0} : " +  e.getStatus());
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

        String targetUrl = "localhost:9090";

        //the generic token without scopes
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik5UZG1aak00WkRrM05qWTBZemM1TW1abU9EZ3dNVEUzTVdZd05E" +
                "RTVNV1JsWkRnNE56YzRaQT09In0.eyJhdWQiOiJodHRwOlwvXC9vcmcud3NvMi5hcGltZ3RcL2dhdGV3YXkiLCJzdWIiOiJh" +
                "ZG1pbkBjYXJib24uc3VwZXIiLCJhcHBsaWNhdGlvbiI6eyJvd25lciI6ImFkbWluIiwidGllclF1b3RhVHlwZSI6InJlcXVlc" +
                "3RDb3VudCIsInRpZXIiOiJVbmxpbWl0ZWQiLCJuYW1lIjoiRGVmYXVsdEFwcGxpY2F0aW9uIiwiaWQiOjEsInV1aWQiOm51bGx" +
                "9LCJzY29wZSI6ImFtX2FwcGxpY2F0aW9uX3Njb3BlIGRlZmF1bHQiLCJpc3MiOiJodHRwczpcL1wvbG9jYWxob3N0Ojk0NDNcL2" +
                "9hdXRoMlwvdG9rZW4iLCJ0aWVySW5mbyI6eyJVbmxpbWl0ZWQiOnsidGllclF1b3RhVHlwZSI6InJlcXVlc3RDb3VudCIsInN0" +
                "b3BPblF1b3RhUmVhY2giOnRydWUsInNwaWtlQXJyZXN0TGltaXQiOjAsInNwaWtlQXJyZXN0VW5pdCI6bnVsbH19LCJrZXl0eX" +
                "BlIjoiUFJPRFVDVElPTiIsInN1YnNjcmliZWRBUElzIjpbeyJzdWJzY3JpYmVyVGVuYW50RG9tYWluIjoiY2FyYm9uLnN1cGVy" +
                "IiwibmFtZSI6Ik15UmV0YWlsU3RvcmUiLCJjb250ZXh0IjoiXC9teXN0b3JlXC8xLjAuMCIsInB1Ymxpc2hlciI6ImFkbWluIi" +
                "widmVyc2lvbiI6IjEuMC4wIiwic3Vic2NyaXB0aW9uVGllciI6IlVubGltaXRlZCJ9XSwiY29uc3VtZXJLZXkiOiJEYWhDa05D" +
                "SDR6Wjl0MGwxNXlBcVNYOEdzZFlhIiwiZXhwIjozNzM1NTM1MzM5LCJpYXQiOjE1ODgwNTE2OTIsImp0aSI6IjBjYjhjNjFjLTA" +
                "3YmEtNGUzNS05ZWE4LTkwY2M0OTJmODM4OCJ9.dhRQcCPJTTZtr6oHhh41nuDyQmSK_hKY6Kquk4oB0koRP10REmXGqG0IVPrKrW" +
                "Y9SZyQvttlmskSIDkp3D-x8bNeI1sjjZUjzh45-JubnvrgSFYWpYJKityxsi85hKpUQCFvHCXNouOQ4NyyZ0xG1sWWhVo0O0nDleU" +
                "xZuoVIbUNF58Oy6btmMM08YE7ps_bqqkbQYHJyAb0smsv5dbIYKURortnOu6lvnewf5SBeJhrkRm49qzTr5dxySp0rmc_n2ib2" +
                "QYRuSoi1nA9-c3KFwOhEPez-JPkOAvfip3cHYwVpyFHNQCvQcHuDrbgaRxTtcxXxVyM0YA-Emr0I1IWYg";

        //Token with the scopes set to "scope_1"
        String tokenWithScopes = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik5UZG1aak00WkRrM05qWTBZemM1TW1abU" +
                "9EZ3dNVEUzTVdZd05ERTVNV1JsWkRnNE56YzRaQT09In0.eyJhdWQiOiJodHRwOlwvXC9vcmcud3NvMi5hcGltZ3RcL2dhd" +
                "GV3YXkiLCJzdWIiOiJhZG1pbkBjYXJib24uc3VwZXIiLCJhcHBsaWNhdGlvbiI6eyJvd25lciI6ImFkbWluIiwidGllclF1b" +
                "3RhVHlwZSI6InJlcXVlc3RDb3VudCIsInRpZXIiOiJVbmxpbWl0ZWQiLCJuYW1lIjoiRGVmYXVsdEFwcGxpY2F0aW9uIiwia" +
                "WQiOjEsInV1aWQiOm51bGx9LCJzY29wZSI6ImFtX2FwcGxpY2F0aW9uX3Njb3BlIHNjb3BlXzEiLCJpc3MiOiJodHRwczpc" +
                "L1wvbG9jYWxob3N0Ojk0NDNcL29hdXRoMlwvdG9rZW4iLCJ0aWVySW5mbyI6e30sImtleXR5cGUiOiJQUk9EVUNUSU9OIiw" +
                "ic3Vic2NyaWJlZEFQSXMiOltdLCJjb25zdW1lcktleSI6ImhhcVljVDZMWkNUUmNXSGlyQnQ2WmRadDV5QWEiLCJleHAiOjE" +
                "1ODg1MjMwNTIsImlhdCI6MTU4ODIyMzA1MiwianRpIjoiYzhkZGVkM2MtNTAyOC00ZmM5LWI1NTYtZmM2NzhiM2M5NjI1In0" +
                ".dt9Au21_-niPXY-O1lOCNYoWbiwHKgvKf_WLK1oYIXcVQTSrOZkAnCARynJVXTe3Q3PJdJ6jVF7EcG7QGaFK1synKNH7qdA" +
                "GW2oxio7oOGNXJdZ5HW5DzrgIKrCRVTKRQ2MZ9spqMpVFfYugB_Fa8bKM6h_tcYI6UUOiNW-l8hIYuiKFgSQ6x6YSsmoI_OC" +
                "Sikfrh3cEGn0E-0chM-HGKxrMLJSuySvHfs98erMfIItqiKWr9MckOCTxfZmFiXy-NzyOS3CUcHgYuRM3Xy3ZsHvVxyp-sU" +
                "nVbK8eY8JS0CTmJwWkD15rtru-pf2rvgVqRZh8Y2H0-J6nami4pUVd3A";


        //Initiate a order client with token details
        OrderClient orderClient = new OrderClient(targetUrl,tokenWithScopes);


        //get the available product list
        orderClient.getItemDetailsFromStore("CITY_A");
        System.out.println(" ----- \n\n");

        //Make an order
        String json = "{\"item\":\"Item_A\", \"quantity\":3, \"location\":\"City_B\"}";
        JSONObject jsonObject = orderClient.order(new JSONObject(json));
        if (jsonObject != null) {
            System.out.println(jsonObject.toString());
        }

        System.out.println(" ----- \n\n");

        //to have product details periodically (server streaming)
//        orderClient.subscribeItemDetails("CITY_A");

    }
}
