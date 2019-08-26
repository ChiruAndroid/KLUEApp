package com.volive.klueapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.models.OccationsModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.OccationsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.volive.klueapp.activities.BaseActivity.checkConnection;

public class SubcategoryActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<OccationsModel> subcategoryList;
    public static OccationsAdapter occationsAdapter;
    ImageView navigation_icon;
    TextView tv_title;
    Toolbar toolbar;
    RecyclerView subcat_recyclerView;
    PreferenceUtils preferenceUtils;
    String catId,title;
    String position;
    MenuItem searchViewItem;
    LinearLayout nav_home, nav_top_pics, nav_categories, nav_whishlist, nav_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subcategory);
        preferenceUtils = new PreferenceUtils(SubcategoryActivity.this);
        initializeUI();

        if (checkConnection()) {
            checkValuesAndCallApi();
        } else
            Toast.makeText(SubcategoryActivity.this, R.string.plz_check_internet_connection, Toast.LENGTH_SHORT).show();
    }

    private void initializeUI() {
        nav_home = findViewById(R.id.nav_home);
        nav_top_pics = findViewById(R.id.nav_top_pics);
        nav_categories = findViewById(R.id.nav_categories);
        nav_whishlist = findViewById(R.id.nav_whishlist);
        nav_account = findViewById(R.id.nav_account);
        nav_home.setOnClickListener(this);
        nav_top_pics.setOnClickListener(this);
        nav_categories.setOnClickListener(this);
        nav_whishlist.setOnClickListener(this);
        nav_account.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tv_title = findViewById(R.id.tv_title);
        navigation_icon = findViewById(R.id.back_button);
        subcat_recyclerView = findViewById(R.id.rview_flowers);
        LinearLayoutManager lManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        subcat_recyclerView.setLayoutManager(lManager);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setUprecyclerView() {

        if (subcategoryList != null && subcategoryList.size() == 0) {
//            Toast.makeText(this, R.string.no_items, Toast.LENGTH_LONG).show();
        } else {
            occationsAdapter = new OccationsAdapter(this, subcategoryList, catId);
            subcat_recyclerView.setAdapter(occationsAdapter);
        }
    }

    public void checkValuesAndCallApi() {
        if (getIntent() != null) {
//            tv_title.setText(Home.category_list.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getCname());
//           catId = Home.category_list.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getCat_id();
            title = getIntent().getStringExtra("title_nme");
            position = getIntent().getStringExtra("Position");
            tv_title.setText(getIntent().getStringExtra("title_nme"));
             catId = getIntent().getStringExtra("cat_id");

            final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.SUB_CAT_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), catId);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
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
                                String base_path = responseObject.getString("base_path");
                                JSONArray subcatArray = responseObject.getJSONArray("data");
                                subcategoryList = new ArrayList<>();
                                for (int i = 0; i < subcatArray.length(); i++) {
                                    JSONObject subcatObject = subcatArray.getJSONObject(i);
                                    OccationsModel occationsModel = new OccationsModel();
                                    occationsModel.setSub_cat_id(subcatObject.getString("sub_cat_id"));
                                    occationsModel.setSub_cat_name(subcatObject.getString("sub_cat_name"));
                                    occationsModel.setSub_cat_image(base_path + subcatObject.getString("sub_cat_image"));
                                    subcategoryList.add(occationsModel);
                                }
                            }
                            setUprecyclerView();
                        } catch (Exception e) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawermenu, menu);
        searchViewItem = menu.findItem(R.id.nav_item_search);
        if (title.equalsIgnoreCase("spa")) {
            searchViewItem.setVisible(true);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
            search(searchView);
        } else {
            searchViewItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                occationsAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_item_cart:
                preferenceUtils = new PreferenceUtils(this);
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    intent = new Intent(this, ShoppingCart.class);
                    startActivity(intent);
                } else {
                    BaseActivity.showDialog(this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_cart), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(SubcategoryActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
                return true;
            case R.id.nav_item_search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_home:
                intent = new Intent(SubcategoryActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MainActivity.rl_header.setVisibility(View.VISIBLE);
                MainActivity.title.setText(getResources().getString(R.string.home));
                break;
            case R.id.nav_top_pics:
                MainActivity.rl_header.setVisibility(View.VISIBLE);
                MainActivity.title.setText(getResources().getString(R.string.top_pics));
                intent = new Intent(SubcategoryActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_categories:
                intent = new Intent(SubcategoryActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "2");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_whishlist:
                intent = new Intent(SubcategoryActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "3");
                startActivity(intent);
                break;
            case R.id.nav_account:
                intent = new Intent(SubcategoryActivity.this, MainActivity.class);
                intent.putExtra("Option", "true");
                intent.putExtra("Position", "4");
                startActivity(intent);
                break;
        }
    }
}