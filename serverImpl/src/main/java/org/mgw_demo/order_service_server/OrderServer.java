package org.mgw_demo.order_service_server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.mgw_demo.order_service_lib.OrderServiceGrpc;
import org.mgw_demo.order_service_lib.OrderServiceOuterClass;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


public class OrderServer {
    private Server server;

    public void start(int port) throws IOException {
        if (server == null || server.isShutdown() || server.isTerminated()) {
            server = ServerBuilder.forPort(port)
                    .addService(new OrderServiceImpl())
                    .build().start();
        }
        System.out.println("Server started, listening on " + port);
    }

    public static void main(String[] args) throws IOException {
        int port ;
        if (args.length == 1)  {
            String portVal = args[0];
            port = Integer.parseInt(portVal);
        } else {
            port = 50001;
        }
        new OrderServer().start(port);

        while(true) {
        }
    }
}

class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
    private String[] items = {"ItemA", "ItemB", "ItemC"};

    private Map<String, Integer> cityAStoreMap = new ConcurrentHashMap<String, Integer>();
    private Map<String, Integer> cityBStoreMap = new ConcurrentHashMap<String, Integer>();
    private Map<String, Integer> cityCStoreMap = new ConcurrentHashMap<String, Integer>();

    public OrderServiceImpl () {
        populateStoreDetails(cityAStoreMap);
        populateStoreDetails(cityBStoreMap);
        populateStoreDetails(cityCStoreMap);
    }

    @Override
    public void placeOrder(OrderServiceOuterClass.OrderRequest request,
                           StreamObserver<OrderServiceOuterClass.OrderResponse> responseObserver) {

        OrderServiceOuterClass.Status status = OrderServiceOuterClass.Status.FAILED;
        int price = 0;
        String description = "Out of stock.";

        //logic goes here
        int quantity = request.getQuantity();
        if (quantity < 5) {
            status = OrderServiceOuterClass.Status.SUCCESSFUL;
            price = 10 * quantity;
            description = " please purchase within today. ";
        }

        OrderServiceOuterClass.OrderResponse response = OrderServiceOuterClass.OrderResponse.newBuilder()
                .setStatus(status)
                .setPrice(price)
                .setDescription(description)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void subscribeItemDetails(OrderServiceOuterClass.StoreLocation request,
                         StreamObserver<OrderServiceOuterClass.ItemDetails> responseObserver) {
       Map<String, Integer> storeMap = getStoreFromLocation(request.getLocation());

       int count = 5;
       while (count > 0) {
           OrderServiceOuterClass.ItemDetails.Builder builder = OrderServiceOuterClass.ItemDetails.newBuilder();

           for (Map.Entry<String, Integer> entry : storeMap.entrySet()) {
               OrderServiceOuterClass.Item item = OrderServiceOuterClass.Item.newBuilder()
                       .setItemName(entry.getKey())
                       .setQuantity(entry.getValue())
                       .build();
               builder.addItem(item);

               //to change the item quantity after each reading
               entry.setValue(entry.getValue() - (new Random().nextInt(5)));
           }
           responseObserver.onNext(builder.build());
           count--;
           try {
               Thread.sleep(2000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       responseObserver.onCompleted();
    }

    @Override
    public void getItemDetails(OrderServiceOuterClass.StoreLocation request,
                               StreamObserver<OrderServiceOuterClass.ItemDetails> responseObserver) {
        Map<String, Integer> storeMap = getStoreFromLocation(request.getLocation());

        OrderServiceOuterClass.ItemDetails.Builder builder = OrderServiceOuterClass.ItemDetails.newBuilder();

        for (Map.Entry<String, Integer> entry : storeMap.entrySet()) {
            OrderServiceOuterClass.Item item = OrderServiceOuterClass.Item.newBuilder()
                    .setItemName(entry.getKey())
                    .setQuantity(entry.getValue())
                    .build();
            builder.addItem(item);

            //to change the item quantity after each reading
            entry.setValue(entry.getValue() - (new Random().nextInt(5)));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private void populateStoreDetails(Map <String, Integer> storeMap) {
        for (String item : items) {
            Random rand = new Random();
            storeMap.put(item, 500 - rand.nextInt(50));
        }
    }

    private Map<String, Integer> getStoreFromLocation(OrderServiceOuterClass.Location location) {
        Map<String, Integer> storeMap = null;
        switch (location) {
            case CITY_A:
                storeMap = cityAStoreMap;
                break;
            case CITY_B:
                storeMap = cityBStoreMap;
                break;
            case CITY_C:
                storeMap = cityCStoreMap;
                break;
        }
        return storeMap;
    }
}
