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
            response = OrderServiceOuterClass.OrderResponse.newBuilder()
                    .setPrice(0)
                    .setStatus(OrderServiceOuterClass.Status.FAILED)
                    .setDescription("Internal Error")
                    .build();
            return responseToJsonObject(response);
        }
    }

    public void subscribeItemDetails (String locationString) {

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
        String targetUrl = "localhost:50001";
        String function = null;
        String input = null;
        String token = null;

//        if (args.length == 3) {
//            function = args[0].toLowerCase();
//            input = args[1];
//            token = args[2];
//        } else if (args.length == 2) {
//            function = args[0].toLowerCase();
//            input = args[1];
//        } else if (args.length > 3) {
//            System.out.println("Invalid number of arguments \n <function> <input> [<targetUrl>] [<token>]");
//        }

        if (args.length > 0) {
            function = args[0].toLowerCase();
        }
        if (args.length > 1) {
            input = args[1];
        }
        if (args.length > 2) {
            targetUrl = args[2];
        }
        if(args.length == 4) {
            token = args[3];
        }

        OrderClient orderClient;
        if (token == null) {
            orderClient = new OrderClient(targetUrl);
        } else {
            orderClient = new OrderClient(targetUrl, token);
        }

        if (function == null) {
            orderClient.getItemDetailsFromStore("CITY_A");
        }

        switch (function) {
            case "placeorder":
                invokePlaceOrder(orderClient, input);
                break;
            case "getdetails":
                invokeGetDetails(orderClient, input);
                break;
            case "subscribe":
                invokeSubscribe(orderClient, input);
                break;
            default:
                System.out.println("function is not recogized");
        }

//        String json = "{\"item\":\"Item_A\", \"quantity\":3, \"location\":\"City_B\"}";
//        OrderClient orderClient = new OrderClient(targetUrl);
//        JSONObject jsonObject = orderClient.order(new JSONObject(json));
//        System.out.println(jsonObject.toString());
//
//        System.out.println(" ----- \n\n");
//
//        orderClient.subscribeItemDetails("CITY_A");
//
//        System.out.println(" ----- \n\n");
//
//        orderClient.getItemDetailsFromStore("CITY_A");

    }

    private static void invokeSubscribe (OrderClient orderClient, String input) {
        if (input == null) {
            System.out.println("Executing the default RPC call.");
            orderClient.subscribeItemDetails("CITY_A");
        } else {
            orderClient.subscribeItemDetails(input);
        }
    }

    private static void invokeGetDetails (OrderClient orderClient, String input) {
        if (input == null) {
            System.out.println("Executing the default RPC call.");
            orderClient.getItemDetailsFromStore("CITY_A");
        } else {
            orderClient.getItemDetailsFromStore(input);
        }
    }

    private static void invokePlaceOrder (OrderClient orderClient, String input) {
        String json;
        if (input == null) {
            System.out.println("Executing the default RPC call.");
            json = "{\"item\":\"Item_A\", \"quantity\":3, \"location\":\"City_B\"}";
        } else {
            json = input;
        }
        JSONObject jsonObject = orderClient.order(new JSONObject(json));
        System.out.println(jsonObject.toString());
    }
}
