package org.mgw_demo.order_service_server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.mgw_demo.order_service_lib.OrderServiceGrpc;
import org.mgw_demo.order_service_lib.OrderServiceOuterClass;

import java.io.IOException;

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

    @Override
    public void order(org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest request,
                      io.grpc.stub.StreamObserver<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse>
                              responseObserver) {

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
}
