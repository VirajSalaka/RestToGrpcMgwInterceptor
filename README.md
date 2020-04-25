**Overview**

Here is a sample gRPC Java based implementation. 
This implemetation is basically for supporting 
REST to gRPC scenario using wso2 microgateway's java 
interceptors.

There is a gRPC server and client implementation for the
"order" Resource. The proto file is as follows.
```
syntax = "proto3";
package org.mgw_demo.order_service_lib;

service OrderService {
    rpc order (OrderRequest) returns (OrderResponse);
}

message OrderRequest {
    string item = 1;
    int32 quantity = 2;
    string location = 3;
}

message OrderResponse {
    Status status = 1;
    int32 price = 2;
    string description = 3;
}

enum Status {
    SUCCESSFUL = 0;
    FAILED = 1;
}
```

gRPC server do not perform any authentication. The server will
execute a simple logic.
- if quantity < 5, order client will receive success with
price is set to 10 * quantity
- else it will set the status to failed saying out of stock 

**How to execute**
- Install Java and Maven and update the PATH variables accordingly.

- Build the project using Maven.

`mvn clean install`

- To run the gRPC server, (server will use the port 50001)

`java -jar serverImpl/target/serverImpl-1.0-SNAPSHOT.jar `

- To run the gRPC client, (for testing purpose. The 
request object in this case is hard coded.)

`java -jar clientImpl/target/clientImpl-1.0-SNAPSHOT.jar`

- To invoke the gRPC service using a REST client using 
microgateway
    - Copy the orderService.yaml to < Microgateway_Project > /api_definitions directory.
    - Copy the following jar files to the < Microgateway_Project > /lib 
    directory
        - mgwGrpcInterceptor/target/mgwGrpcInterceptor-1.0-SNAPSHOT.jar
        - clientImpl/target/clientImpl-1.0-SNAPSHOT.jar
        - all the jars inside clientImpl/target/lib directory
    - build and run the microgateway.
    - invoke the gateway with the following url.
    
        - curl http://localhost:9090/retailStore/order -X POST -d '{"item":"Cake", "quantity":3, "location":"Colombo"}' -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN"
