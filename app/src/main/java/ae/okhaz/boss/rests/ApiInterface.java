package ae.okhaz.boss.rests;


import ae.okhaz.boss.rests.Requests.RequestDeliveryBoy;
import ae.okhaz.boss.rests.Response.ResponseBranch;
import ae.okhaz.boss.rests.Response.ResponseCheckStatus;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseDailySalesDashboard;
import ae.okhaz.boss.rests.Response.ResponseDashboard;
import ae.okhaz.boss.rests.Response.ResponseDeliveryBoyList;
import ae.okhaz.boss.rests.Response.ResponseDeliveryMan;
import ae.okhaz.boss.rests.Response.ResponseDeliverymanDetails;
import ae.okhaz.boss.rests.Response.ResponseHomeSlider;
import ae.okhaz.boss.rests.Response.ResponseLogin;
import ae.okhaz.boss.rests.Response.ResponseNotification;
import ae.okhaz.boss.rests.Response.ResponseOrderAdminList;
import ae.okhaz.boss.rests.Response.ResponseOrderDetails;
import ae.okhaz.boss.rests.Response.ResponseOrderList;
import ae.okhaz.boss.rests.Response.ResponseOrderStatus;
import ae.okhaz.boss.rests.Response.ResponseProductList;
import ae.okhaz.boss.rests.Response.ResponseSupplier;

import ae.okhaz.boss.rests.Response.ResponseSupplierList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {


    @POST("login")
    @FormUrlEncoded
    Call<ResponseLogin> loginProcess(@Field("userName") String userName,
                                     @Field("passWord") String passWord);


    @POST("getOrders")
    @FormUrlEncoded
    Call<ResponseOrderList> getOrders(@Field("DeliveryMan") String DeliveryMan,
                                      @Field("BranchCode") String BranchCode ,
                                      @Field("OrderStatus") String OrderStatus,
                                      @Field("offset") String offset,
                                      @Field("condition") String condition,
                                      @Field("OrderID") String OrderID,
                                      @Field("startDate") String startDate,
                                      @Field("endDate") String endDate,
                                      @Field("filter") String filter);


    @POST("getOwnerOrders")
    @FormUrlEncoded
    Call<ResponseOrderAdminList> getOwnerOrders(@Field("ownerID") String OwnerID,
                                                @Field("DeliveryMan") String DeliveryMan,
                                                @Field("BranchCode") String BranchCode ,
                                                @Field("role") String role ,
                                                @Field("OrderStatus") String OrderStatus,
                                                @Field("offset") String offset,
                                                @Field("condition") String condition,
                                                @Field("OrderID") String OrderID,
                                                @Field("startDate") String startDate,
                                                @Field("endDate") String endDate,
                                                @Field("filter") String filter);


    @POST("getSupplierDetails")
    @FormUrlEncoded
    Call<ResponseSupplier> getSupplierDetails(@Field("userId") String userId);

    @POST("getSuppliers")
    @FormUrlEncoded
    Call<ResponseSupplierList> getSuppliers(@Field("BranchCode") String BranchCode,
                                            @Field("offset") String offset,
                                            @Field("keyword") String keyword,
                                            @Field("sort") String sort);

    @POST("updateSupplierStatus")
    @FormUrlEncoded
    Call<ResponseCommon> updateSupplierStatus(@Field("BranchId") String BranchId,
                                              @Field("suppStatus") String suppStatus,
                                              @Field("suppID") String suppID);

    @POST("updateProductStockStatus")
    @FormUrlEncoded
    Call<ResponseCommon> updateProductStockStatus(@Field("BranchCode") String BranchCode,
                                              @Field("StockingType") String StockingType,
                                              @Field("ItemId") String ItemId);

    @POST("updateProductStatus")
    @FormUrlEncoded
    Call<ResponseCommon> updateProductStatus(@Field("BranchCode") String BranchCode,
                                              @Field("Status") String Status,
                                             @Field("ItemId") String ItemId);

    @POST("orderDetail")
    @FormUrlEncoded
    Call<ResponseOrderDetails> orderDetail(@Field("orderID") String orderID);

    @POST("ownerOrderDetail")
    @FormUrlEncoded
    Call<ResponseOrderDetails> ownerOrderDetail(@Field("orderID") String orderID,
                                                @Field("role") String role,
                                                @Field("orderType") String orderType);

    @POST("supplierOrderDetail")
    @FormUrlEncoded
    Call<ResponseOrderDetails> supplierOrderDetail(@Field("orderID") String orderID,@Field("subOrderCode") String subOrderCode);


    @POST("updateFirebaseToken")
    @FormUrlEncoded
    Call<ResponseCommon> updateFirebaseId(@Field("id") String id, @Field("AccessToken") String token);

    @POST("deliverymanDetails")
    @FormUrlEncoded
    Call<ResponseDeliverymanDetails> deliverymanDetails(@Field("id") String id);

    @POST("updateOrderStatus")
    @FormUrlEncoded
    Call<ResponseCommon> updateOrderStatus(@Field("DeliveryMan") String DeliveryMan, @Field("OrderID") String OrderID , @Field("OrderStatus") String OrderStatus);

    @POST("updateLoginStatus")
    @FormUrlEncoded
    Call<ResponseCheckStatus> updateLoginStatus(@Field("UserId") String id,
                                                @Field("loginstatus") String loginstatus,
                                                @Field("branchid") String BranchCode,
                                                @Field("Aid") String Aid );

    @POST("checkLoginStatus")
    @FormUrlEncoded
    Call<ResponseCheckStatus> checkLoginStatus(@Field("UserId") String UserId);


    @POST("updateBarcodeScanStatus")
    @FormUrlEncoded
    Call<ResponseCommon> updateBarcodeScanStatus(@Field("scanStatus") String scanStatus,@Field("OrderItemId") String OrderItemId);

    @POST("updateBarcode")
    @FormUrlEncoded
    Call<ResponseCommon> updateBarcode(@Field("barcode") String barcode,@Field("OrderItemId") String OrderItemId,@Field("itemId") String itemId);

    @POST("addComment")
    @FormUrlEncoded
    Call<ResponseCommon> AddComment(@Field("Message") String Message
                                    ,@Field("userID") String userID
                                    ,@Field("OrderID") String OrderID
                                    ,@Field("BranchID") String BranchID
                                    ,@Field("pStatus") String pStatus
                                    );
    @POST("viewdeliveryman")
    @FormUrlEncoded
    Call<ResponseDeliveryBoyList> deliveryBoyList(
            @Field("ID") String ID
            ,@Field("role") String role
            ,@Field("BranchCode") String BranchCode
    );

    @POST("getdeliverymanbyid")
    @FormUrlEncoded
    Call<ResponseDeliveryMan> getdeliverymanbyid(
            @Field("StaffId") String StaffId
    );

    @POST("updateLalLong")
    @FormUrlEncoded
    Call<ResponseCommon> updateLatLng(
            @Field("id") String id
            ,@Field("latitude") String latitude
            ,@Field("longitude") String longitude
    );

    @POST("adddeliveryman")
    Call<ResponseCommon> addDeliveryBoy(@Body RequestDeliveryBoy requestDeliveryBoy);

    @POST("editdeliveryman")
    Call<ResponseCommon> editDeliveryBoy(@Body RequestDeliveryBoy requestDeliveryBoy);

    @POST("deletedeliveryman")
    Call<ResponseCommon> deleteDeliveryBoy(@Body RequestDeliveryBoy requestDeliveryBoy);

    @POST("updateOwnerOrderStatus")
    @FormUrlEncoded
    Call<ResponseCommon> updateOrderStatusAdmin(@Field("OrderStatus") String OrderStatus,
                                                @Field("OrderID") String OrderID,
                                                @Field("role") String role,
                                                @Field("ownerID") String ownerID,
                                                @Field("orderType") String orderType,
                                                 @Field("reason") String reason);

    @POST("updateOwnerOrderDeliveryMan")
    @FormUrlEncoded
    Call<ResponseCommon> updateOwnerOrderDeliveryMan(@Field("DeliveryMan") String OrderStatus,
                                                     @Field("OrderID") String OrderID,
                                                     @Field("role") String role,
                                                     @Field("ownerID") String ownerID,
                                                     @Field("orderType") String orderType,
                                                     @Field("reason") String reason);


    @GET("getOrderStatus")
    Call<ResponseOrderStatus> getOrderStatus();

    @GET("getBranches")
    Call<ResponseBranch> getBranches();

    @GET("getProducts")
    Call<ResponseProductList> getProducts(@Query("branchCode") String branchCode,
                                          @Query("supplierID") String supplierID,
                                          @Query("offset") String offset,
                                          @Query("keyword") String keyword);

    @POST("getSlider")
    @FormUrlEncoded
    Call<ResponseHomeSlider> getSlider(@Field("BranchCode") String BranchCode);

    @POST("getOrderStatusWithOrderCount")
    @FormUrlEncoded
    Call<ResponseOrderStatus> getOrderStatusCount(@Field("role") String UserId,
                                                  @Field("ownerID") String BranchId,
                                                  @Field("BranchCode") String BranchCode,
                                                  @Field("filter") String filter);


    @POST("getSales")
    @FormUrlEncoded
    Call<ResponseDashboard> getDashboardData(@Field("UserId") String UserId);

    @POST("getDailySales")
    @FormUrlEncoded
    Call<ResponseDailySalesDashboard> getDailySaleData(@Field("UserId") String UserId);

    @POST("getNotifications")
    @FormUrlEncoded
    Call<ResponseNotification> getNotifications(@Field("UserID") String UserId,
                                                @Field("BranchId") String BranchId);

}
