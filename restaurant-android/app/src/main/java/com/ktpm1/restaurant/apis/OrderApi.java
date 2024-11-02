    package com.ktpm1.restaurant.apis;

    import com.ktpm1.restaurant.dtos.requests.BookingTableRequest;
    import com.ktpm1.restaurant.dtos.responses.InvoiceResponse;
    import com.ktpm1.restaurant.dtos.responses.ResponseMessage;
    import com.ktpm1.restaurant.dtos.responses.TableResponse;
    import com.ktpm1.restaurant.dtos.responses.VNPayMessage;
    import com.ktpm1.restaurant.models.Order;
    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.GET;
    import retrofit2.http.Header;
    import retrofit2.http.POST;
    import retrofit2.http.Header;
    import retrofit2.http.Query;

    import java.util.List;

    public interface OrderApi {
        @POST("/orders/create-food-and-table")
        Call<VNPayMessage> createOrder(@Header("Authorization") String token,
                                       @Body BookingTableRequest bookingTableRequest);

        @POST("/orders/create-table-only")
        Call<List<Order>> createOrderTableOnly(@Header("Authorization") String token);

        @POST("/orders/create-food-only")
        Call<ResponseMessage> createOrderFoodOnly(@Header("Authorization") String token);

        // Thêm phương thức lấy danh sách bàn đã đặt
        @GET("/orders/my-orders")
        Call<List<TableResponse>> getReservedTables(@Header("Authorization") String token);

        Call<List<Order>> getOrderedFood(String token);

        Call<List<InvoiceResponse>> getInvoiceList(String token);
    }