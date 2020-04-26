package org.mgw_demo.order_service_lib;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.27.0)",
    comments = "Source: order_service.proto")
public final class OrderServiceGrpc {

  private OrderServiceGrpc() {}

  public static final String SERVICE_NAME = "org.mgw_demo.order_service_lib.OrderService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest,
      org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse> getOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "order",
      requestType = org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest.class,
      responseType = org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest,
      org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse> getOrderMethod() {
    io.grpc.MethodDescriptor<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest, org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse> getOrderMethod;
    if ((getOrderMethod = OrderServiceGrpc.getOrderMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getOrderMethod = OrderServiceGrpc.getOrderMethod) == null) {
          OrderServiceGrpc.getOrderMethod = getOrderMethod =
              io.grpc.MethodDescriptor.<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest, org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "order"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("order"))
              .build();
        }
      }
    }
    return getOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest,
      org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse> getNotifyMeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "notifyMe",
      requestType = org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest.class,
      responseType = org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest,
      org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse> getNotifyMeMethod() {
    io.grpc.MethodDescriptor<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest, org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse> getNotifyMeMethod;
    if ((getNotifyMeMethod = OrderServiceGrpc.getNotifyMeMethod) == null) {
      synchronized (OrderServiceGrpc.class) {
        if ((getNotifyMeMethod = OrderServiceGrpc.getNotifyMeMethod) == null) {
          OrderServiceGrpc.getNotifyMeMethod = getNotifyMeMethod =
              io.grpc.MethodDescriptor.<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest, org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "notifyMe"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("notifyMe"))
              .build();
        }
      }
    }
    return getNotifyMeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OrderServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderServiceStub>() {
        @java.lang.Override
        public OrderServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderServiceStub(channel, callOptions);
        }
      };
    return OrderServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OrderServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingStub>() {
        @java.lang.Override
        public OrderServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderServiceBlockingStub(channel, callOptions);
        }
      };
    return OrderServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OrderServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OrderServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OrderServiceFutureStub>() {
        @java.lang.Override
        public OrderServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OrderServiceFutureStub(channel, callOptions);
        }
      };
    return OrderServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class OrderServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void order(org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest request,
        io.grpc.stub.StreamObserver<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getOrderMethod(), responseObserver);
    }

    /**
     */
    public void notifyMe(org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest request,
        io.grpc.stub.StreamObserver<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNotifyMeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getOrderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest,
                org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse>(
                  this, METHODID_ORDER)))
          .addMethod(
            getNotifyMeMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest,
                org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse>(
                  this, METHODID_NOTIFY_ME)))
          .build();
    }
  }

  /**
   */
  public static final class OrderServiceStub extends io.grpc.stub.AbstractAsyncStub<OrderServiceStub> {
    private OrderServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderServiceStub(channel, callOptions);
    }

    /**
     */
    public void order(org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest request,
        io.grpc.stub.StreamObserver<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyMe(org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest request,
        io.grpc.stub.StreamObserver<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getNotifyMeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class OrderServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<OrderServiceBlockingStub> {
    private OrderServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse order(org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest request) {
      return blockingUnaryCall(
          getChannel(), getOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse> notifyMe(
        org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getNotifyMeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class OrderServiceFutureStub extends io.grpc.stub.AbstractFutureStub<OrderServiceFutureStub> {
    private OrderServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OrderServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OrderServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse> order(
        org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getOrderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ORDER = 0;
  private static final int METHODID_NOTIFY_ME = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final OrderServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(OrderServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ORDER:
          serviceImpl.order((org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderRequest) request,
              (io.grpc.stub.StreamObserver<org.mgw_demo.order_service_lib.OrderServiceOuterClass.OrderResponse>) responseObserver);
          break;
        case METHODID_NOTIFY_ME:
          serviceImpl.notifyMe((org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyRequest) request,
              (io.grpc.stub.StreamObserver<org.mgw_demo.order_service_lib.OrderServiceOuterClass.NotifyResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class OrderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OrderServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.mgw_demo.order_service_lib.OrderServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OrderService");
    }
  }

  private static final class OrderServiceFileDescriptorSupplier
      extends OrderServiceBaseDescriptorSupplier {
    OrderServiceFileDescriptorSupplier() {}
  }

  private static final class OrderServiceMethodDescriptorSupplier
      extends OrderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    OrderServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (OrderServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OrderServiceFileDescriptorSupplier())
              .addMethod(getOrderMethod())
              .addMethod(getNotifyMeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
