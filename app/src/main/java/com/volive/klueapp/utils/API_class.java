package com.volive.klueapp.utils;

import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API_class {

    @FormUrlEncoded
    @POST(DZ_URL.USER_REGISTRATION)
    Call<JsonElement> USER_REGISTER(@Field("fname") String fname, @Field("email") String email,
                                    @Field("mobile") String mobile, @Field("password") String password,
                                    @Field("device_type") String device_type, @Field("lang") String lang,
                                    @Field("device_token") String device_token, @Field("API-KEY") String api_key);

    @FormUrlEncoded
    @POST(DZ_URL.LOGIN_SERVICE)
    Call<JsonElement> USER_LOGIN(@Field("API-KEY") String api_key, @Field("email") String email,
                                 @Field("password") String password, @Field("lang") String lang,
                                 @Field("device_type") String device_type, @Field("device_token") String device_token);

    @GET(DZ_URL.GET_PROFILE_SERVICE)
    Call<JsonElement> GET_PROFILE(@Query("API-KEY") String apikey, @Query("lang") String lang,
                                  @Query("user_id") String user_id);

    @FormUrlEncoded
    @POST(DZ_URL.RESET_PASSWORD)
    Call<JsonElement> RESET_PASSWORD(@Field("API-KEY") String api_key, @Field("lang") String lang,
                                     @Field("user_id") String user_id, @Field("old_pass") String old_pass,
                                     @Field("new_pass") String new_pass, @Field("con_pass") String con_pass);

    @Multipart
    @POST(DZ_URL.UPDATE_PROFILE)
    Call<JsonElement> UPDATE_PROFILE(@Part("API-KEY") RequestBody api_key, @Part("lang") RequestBody lang,
                                     @Part("user_id") RequestBody user_id, @Part("fname") RequestBody fname,
                                     @Part("email") RequestBody email, @Part("mobile") RequestBody mobile,
                                     @Part MultipartBody.Part profile_image);

    @GET(DZ_URL.HOME_SERVICE)
    Call<JsonElement> HOME_FRAGMENT_SERVICE(@Query("API-KEY") String apikey, @Query("lang") String lang,
                                            @Query("user_id") String user_id);

    @GET(DZ_URL.SUB_CAT_SERVICE)
    Call<JsonElement> SUB_CAT_SERVICE(@Query("API-KEY") String apikey, @Query("lang") String lang, @Query("cat_id") String cat_id);

    @GET(DZ_URL.PRODUCT_INFO_SERVICE)
    Call<JsonElement> PRODUCT_INFO(@Query("API-KEY") String apikey, @Query("lang") String lang, @Query("prod_id") String prod_id,
                                   @Query("user_id") String user_id);

    @FormUrlEncoded
    @POST(DZ_URL.SUB_PRODUCT_LIST)
    Call<JsonElement> SUB_PRODUCT_LIST_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                               @Field("sub_cat_id") String sub_cat_id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(DZ_URL.CART_SERVICE)
    Call<JsonElement> ADD_CART_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                       @Field("prod_id") String prod_id, @Field("user_id") String user_id);
    @FormUrlEncoded
    @POST(DZ_URL.CART_SERVICE)
    Call<JsonElement> ADD_CART_SERVICE1(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                        @Field("prod_id") String prod_id, @Field("user_id") String user_id,
                                        @Field("quantity") String quantity);
    @FormUrlEncoded
    @POST(DZ_URL.CART_SERVICE_Decrement)
    Call<JsonElement> DECREMENT_CART_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                              @Field("prod_id") String prod_id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(DZ_URL.CART_SERVICE_Decrement)
    Call<JsonElement> DECREMENT_CART_SERVICE1(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                        @Field("prod_id") String prod_id, @Field("user_id") String user_id,
                                        @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST(DZ_URL.GET_CART_SERVICE)
    Call<JsonElement> GET_CART_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                       @Field("user_id") String user_id,@Field("currency_type") String currency_type);

    @FormUrlEncoded
    @POST(DZ_URL.ADD_WHISLIST)
    Call<JsonElement> ADD_WHISLIST_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                           @Field("user_id") String user_id, @Field("prod_id") String prod_id);

    @FormUrlEncoded
    @POST(DZ_URL.GET_WHISLIST)
    Call<JsonElement> GET_WHISLIST_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                           @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(DZ_URL.DEL_CART)
    Call<JsonElement> DEL_CART_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                       @Field("user_id") String user_id, @Field("prod_id") String prod_id);

    @FormUrlEncoded
    @POST(DZ_URL.DEL_FAVOURITE)
    Call<JsonElement> DEL_FAVOURITE_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                            @Field("user_id") String user_id, @Field("prod_id") String prod_id);

    @FormUrlEncoded
    @POST(DZ_URL.ADD_NEW_ADDRESS)
    Call<JsonElement> ADD_NEW_ADDRESS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                              @Field("user_id") String user_id,@Field("username") String username,
                                              @Field("email") String email,@Field("address") String address,
                                              @Field("city") String city, @Field("shipping_id") String shipping_id,
                                              @Field("landmark") String landmark,@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(DZ_URL.GET_ADDRESS)
    Call<JsonElement> GET_ADDRESS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                          @Field("user_id") String user_id);
    @FormUrlEncoded
    @POST(DZ_URL.DEL_ADDRESS)
    Call<JsonElement> DEL_ADDRESS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                          @Field("shipping_id") String shipping_id,@Field("user_id") String user_id);
    @FormUrlEncoded
    @POST(DZ_URL.HOT_PICS)
    Call<JsonElement> HOT_PICS_LIST(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                    @Field("user_id") String user_id);
    @FormUrlEncoded
    @POST(DZ_URL.MY_ORDERS)
    Call<JsonElement> MY_ORDERS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                        @Field("user_id") String user_id);
    @FormUrlEncoded
    @POST(DZ_URL.MY_ORDER_DETAILS)
    Call<JsonElement> MY_ORDER_DETAILS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                               @Field("user_id") String user_id,@Field("order_id") String order_id);
    @FormUrlEncoded
    @POST(DZ_URL.GIFT_PRODUCTS)
    Call<JsonElement> GIFT_PRODUCTS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang);

    @FormUrlEncoded
    @POST(DZ_URL.CITI_LIST)
    Call<JsonElement> CITI_LIST_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang);

    @FormUrlEncoded
    @POST(DZ_URL.COUNTRY_CODES)
    Call<JsonElement> COUNTRY_LIST_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang);

    @FormUrlEncoded
    @POST(DZ_URL.SHIPPING_METHODS)
    Call<JsonElement> SHIPPING_METHODS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                               @Field("currency_type") String currency_type,@Field("user_id") String user_id);

    @Multipart
    @POST(DZ_URL.BOOKING_ORDERS)
    Call<JsonElement> BOOKING_ORDERS_SERVICE(@Part("API-KEY") RequestBody apikey, @Part("lang") RequestBody lang,@Part("user_id") RequestBody user_id,
                                             @Part("shipping_id") RequestBody shipping_id,
                                             @Part("currency_type") RequestBody currency_type,@Part("message") RequestBody message,
                                             @Part("note") RequestBody note,@Part("payment_type") RequestBody payment_type,
                                             @Part("cod_price") RequestBody cod_price,@Part("delivery_date") RequestBody delivery_date,
                                             @Part("delivery_time") RequestBody delivery_time,@Part MultipartBody.Part file_upload);

    @FormUrlEncoded
    @POST(DZ_URL.FORGOT_PWD)
    Call<JsonElement> FORGOT_PWD_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,@Field("email") String email);

    @FormUrlEncoded
    @POST(DZ_URL.BRAND)
    Call<JsonElement> BRAND_LIST_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang);

    @FormUrlEncoded
    @POST(DZ_URL.FILTERS)
    Call<JsonElement> FILTERS_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,@Field("user_id") String user_id,@Field("sub_cat_id") String sub_cat_id,
                                      @Field("brand_id") String brand_id,@Field("low_price") String low_price,
                                      @Field("high_price") String high_price);

    @FormUrlEncoded
    @POST(DZ_URL.SORT_BY)
    Call<JsonElement> SORT_BY_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,@Field("user_id") String user_id,@Field("sub_cat_id") String sub_cat_id,
                                      @Field("low_price") String low_price,@Field("high_price") String high_price,
                                      @Field("new_arrival") String new_arrival);

    @FormUrlEncoded
    @POST(DZ_URL.HELP_CENTER)
    Call<JsonElement> HELP_CENTER(@Field("API-KEY") String apikey, @Field("lang") String lang);

    @FormUrlEncoded
    @POST(DZ_URL.GIFT_addcart)
    Call<JsonElement> GIFT_ADDCART_SERVICE(@Field("API-KEY") String apikey, @Field("lang") String lang,
                                        @Field("prod_id") String prod_id, @Field("user_id") String user_id,
                                        @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST(DZ_URL.COD)
    Call<JsonElement> COD_List(@Field("API-KEY") String apikey, @Field("lang") String lang);
}
