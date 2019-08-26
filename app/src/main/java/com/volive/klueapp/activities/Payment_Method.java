package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.IPaymentRequestCallBack;
import com.volive.klueapp.utils.MyAddressHelper;
import com.volive.klueapp.utils.PayFortData;
import com.volive.klueapp.utils.PayFortPayment;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.AddressAdapter;

import org.json.JSONObject;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment_Method extends AppCompatActivity implements IPaymentRequestCallBack {
    Button payment;
    LinearLayout layout_payment;
    PreferenceUtils preferenceUtils;
    Toolbar rl_header;
    ImageView navigation_icon;
    String address_id;
    File file = null;
    String st_cod_price = "", st_delivery_date = "", st_delivery_time = "";
    String msg, note, img_path;
    String strPaymentType = "";
    RadioButton rbDebit, rbCredit, rbCOD;
    String deviceId = "", sdkToken = "";
    private FortCallBackManager fortCallback = null;
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d("MIME_TYPE_EXT", extension);
        if (extension != null && extension != "") {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            //  Log.d("MIME_TYPE", type);
        } else {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            type = fileNameMap.getContentTypeFor(url);
        }
        return type;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.payment_method);
        preferenceUtils = new PreferenceUtils(Payment_Method.this);
        preferenceUtils.getStringFromPreference(PreferenceUtils.City, "");

        img_path = getIntent().getStringExtra("img_path");
        msg = getIntent().getStringExtra("msg");
        note = getIntent().getStringExtra("note");
        st_delivery_date = getIntent().getStringExtra("delivery_dt");
        st_delivery_time = getIntent().getStringExtra("delivery_tym");

        initializeUI();
        paymentFort();
    }

    private void initializeUI() {
        layout_payment = findViewById(R.id.layout_payment);
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.select_payment_mode);
        rbCredit = findViewById(R.id.rbCredit);
        rbDebit = findViewById(R.id.rbDebit);
        rbCOD = findViewById(R.id.rbCOD);
        rbCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_payment.setVisibility(View.GONE);
                strPaymentType = "0";
                cod_listService();
            }
        });
        rbDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPaymentType = "1";
                cod_listService();
                layout_payment.setVisibility(View.GONE);
            }
        });
        rbCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPaymentType = "1";
                layout_payment.setVisibility(View.GONE);
            }
        });


        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        payment = (Button) findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strPaymentType.equalsIgnoreCase("0")) {
                    address_id = MyAddressHelper.address_list_array.get(AddressAdapter.addressPosition).getShipping_id();
                    bookingOrdersService();
                } else if (strPaymentType.equalsIgnoreCase("1")) {
                    requestForPayfortPayment();
                } else {
                    Toast.makeText(Payment_Method.this, R.string.plz_select_payment_method, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void bookingOrdersService() {
        MultipartBody.Part image_profile = null;
        if (img_path != null) {
            file = new File(img_path);
            RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(img_path)), file);
            image_profile = MultipartBody.Part.createFormData("file_upload", file.getName(), requestBody);
            Log.d("Image", ">>>>>>>>>>" + image_profile);
        }

        RequestBody r_lang = RequestBody.create(MediaType.parse("multipart/form-data"), preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));
        RequestBody r_api_key = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.API_KEY);
        RequestBody r_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));
        RequestBody r_currency_type = RequestBody.create(MediaType.parse("multipart/form-data"), preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, ""));
        RequestBody r_msg = RequestBody.create(MediaType.parse("multipart/form-data"), msg);
        RequestBody r_note = RequestBody.create(MediaType.parse("multipart/form-data"), note);
        RequestBody r_ship_id = RequestBody.create(MediaType.parse("multipart/form-data"), address_id);
        RequestBody r_payment_type = RequestBody.create(MediaType.parse("multipart/form-data"), strPaymentType);
        RequestBody r_cod_price = RequestBody.create(MediaType.parse("multipart/form-data"), st_cod_price);
        RequestBody r_del_time = RequestBody.create(MediaType.parse("multipart/form-data"), st_delivery_time);
        RequestBody r_del_date = RequestBody.create(MediaType.parse("multipart/form-data"), st_delivery_date);

        preferenceUtils = new PreferenceUtils(Payment_Method.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.BOOKING_ORDERS_SERVICE(r_api_key, r_lang, r_user_id, r_ship_id, r_currency_type, r_msg, r_note, r_payment_type, r_cod_price, r_del_date, r_del_time, image_profile);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");

                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responseObject = new JSONObject(searchResponse);
                        int status = responseObject.getInt("status");

                        if (status == 1) {
                            String message = responseObject.getString("message");
                            Toast.makeText(Payment_Method.this, message, Toast.LENGTH_SHORT);
                            Intent intent = new Intent(Payment_Method.this, PaymentSuccessful.class);
                            intent.putExtra("message", responseObject.getString("message"));
                            intent.putExtra("order_id", responseObject.getString("order_id"));
                            intent.putExtra("total_amount", responseObject.getString("total_amount"));
                            intent.putExtra("payment_type", responseObject.getString("payment_type"));
                            intent.putExtra("order_date", responseObject.getString("order_date"));
                            startActivity(intent);
                        }

                    } catch (Exception e) {
//                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    public void cod_listService() {
        preferenceUtils = new PreferenceUtils(Payment_Method.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.COD_List(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.dismiss();
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responseObject = new JSONObject(searchResponse);
                        int status = responseObject.getInt("status");

                        if (status == 1) {
                            JSONObject jsonObject = responseObject.getJSONObject("data");
//                            String id=responseObject.getString("id");
                            if (preferenceUtils.getStringFromPreference(PreferenceUtils.City, "").equalsIgnoreCase("Riyadh")) {
                                st_cod_price = getString(Integer.parseInt("0.00"));
                            } else {
                                if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.SAR)) {
                                    st_cod_price = jsonObject.getString("cod_price_sar");
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.AED)) {
                                    st_cod_price = jsonObject.getString("cod_price_aed");
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.KWD)) {
                                    st_cod_price = jsonObject.getString("cod_price_kwd");
                                } else if (preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "").equalsIgnoreCase(Constants.USD)) {
                                    st_cod_price = jsonObject.getString("cod_price_usd");
                                } else {
                                    st_cod_price = jsonObject.getString("cod_price_sar");
                                }
                            }
                        }

                    } catch (Exception e) {
//                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    private void paymentFort() {
        fortCallback = FortCallback.Factory.create();
    }

    private void requestForPayfortPayment() {
        PayFortData payFortData = new PayFortData();
        String pdt_amount = ShoppingCart.total_price.getText().toString().trim().replace(preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, ""), "");
        String del_amount = ShippingMethodsActivity.tv_delevery_price.getText().toString().trim().replace(preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, ""), "");

        String amount = String.valueOf(Double.parseDouble(pdt_amount) + Double.parseDouble(del_amount));
        payFortData.amount = String.valueOf((int) Double.parseDouble(amount) * 100);// Multiplying with 100, bcz amount should not be in decimal format
        payFortData.command = PayFortPayment.PURCHASE;
//        payFortData.currency = PayFortPayment.CURRENCY_TYPE;
        payFortData.currency = preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "");
        payFortData.customerEmail = preferenceUtils.getStringFromPreference(PreferenceUtils.Email, "");
        payFortData.language = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");
        payFortData.merchantReference = String.valueOf(System.currentTimeMillis());

        PayFortPayment payFortPayment = new PayFortPayment(this, this.fortCallback, (IPaymentRequestCallBack) this);
        payFortPayment.requestForPayment(payFortData);
    }

    @Override
    public void onPaymentRequestResponse(int responseType, final PayFortData responseData) {
        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
            Toast.makeText(this, "Token not generated", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Token not generated");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
            Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment cancelled");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment failed");
        } else {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
            address_id = MyAddressHelper.address_list_array.get(AddressAdapter.addressPosition).getShipping_id();
            bookingOrdersService();
            Log.e("onPaymentResponse", "Payment successful");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayFortPayment.RESPONSE_PURCHASE) {
            fortCallback.onActivityResult(requestCode, resultCode, data);
        }
    }
}