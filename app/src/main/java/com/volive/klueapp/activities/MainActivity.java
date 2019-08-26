package com.volive.klueapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.klueapp.fragments.CategoryScreen;
import com.volive.klueapp.fragments.Home;
import com.volive.klueapp.fragments.MyAccount;
import com.volive.klueapp.fragments.TopPicsFragment;
import com.volive.klueapp.fragments.Whishlist;
import com.volive.klueapp.models.Category_Model;
import com.volive.klueapp.models.NewArrivalModel;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.BottomNavigationViewHelper;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.ViewPagerAdapter;
import com.volive.klueapp.adpaters.nav_menu_item_adapter;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static ViewPager viewPager;
    public static TextView title;
    public static Toolbar rl_header;
    public static DrawerLayout mDrawerLayout;
    public static ArrayList<Category_Model> category_list;
    public static ArrayList<NewArrivalModel> new_arrival_list;
    RecyclerView navi_menu;
    NavigationView nav_view;
    MenuItem prevMenuItem;
    ImageView navigation_icon, nav_pro_image;
    TextView logout, help_center, nav_pro_name, language_change, Currency_changer, tv_notifications, text_chat;
    String user_id;
    BottomNavigationView bottomNavigationView;
    LinearLayout navmyorder, navmyaddress, navmywishlist, headerView;
    PreferenceUtils preferenceUtils;
    String strSelectLanguage = "";
    String strCurrency = "";
    String languageToLoad, frgmnt_position;
    ArrayList<Category_Model> category_list_array;

    public static void changeFragment(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        preferenceUtils = new PreferenceUtils(this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        initializeUI();
        initializeValues();

        ZopimChat.init("5BAEI78Uph6ox8lsFHCndZ6XmT3KYIMT");

        VisitorInfo visitorData = new VisitorInfo.Builder()
                .name("Visitor name")
                .email("visitor@example.com")
                .phoneNumber("0123456789")
                .note("Your note goes here")
                .build();
        ZopimChat.setVisitorInfo(visitorData);

        home_fragment_service(this);
        nav_pro_image = findViewById(R.id.nav_profile_image);
        nav_pro_name = findViewById(R.id.nav_profile_name);

        frgmnt_position = getIntent().getStringExtra("Position");
        if (frgmnt_position != null) {
            changeFragment(0);
            changeFragment(Integer.parseInt(frgmnt_position));
            bottomNavigationView.getMenu().getItem(Integer.parseInt(frgmnt_position)).setChecked(true);
            if (frgmnt_position.equalsIgnoreCase("0")) {
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.home));
            } else if (frgmnt_position.equalsIgnoreCase("1")) {
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.top_pics));
            } else if (frgmnt_position.equalsIgnoreCase("2")) {
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.categories));
            } else if (frgmnt_position.equalsIgnoreCase("3")) {
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.whislist));
            } else if (frgmnt_position.equalsIgnoreCase("4")) {
                rl_header.setVisibility(View.GONE);
                title.setText(getResources().getString(R.string.account));
            }
        }

        try {
            if (!user_id.equalsIgnoreCase("")) {
                if (!preferenceUtils.getStringFromPreference(PreferenceUtils.BASE_PATH, "").equalsIgnoreCase("") && !preferenceUtils.getStringFromPreference(PreferenceUtils.IMAGE, "").equalsIgnoreCase("")) {
                    Glide.with(context)
                            .load(preferenceUtils.getStringFromPreference(PreferenceUtils.BASE_PATH, "") + preferenceUtils.getStringFromPreference(PreferenceUtils.IMAGE, ""))
                            .into(nav_pro_image);
                }
                nav_pro_name.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.User_name, ""));
            } else {
                nav_pro_name.setText(R.string.guest);
                nav_pro_image.setImageResource(R.drawable.profile_image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            nav_pro_image.setImageResource(R.drawable.profile_image);
            nav_pro_name.setText(getResources().getString(R.string.guest));
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_home:
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.home));
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.btn_top_pics:
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.top_pics));
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.btn_catogerys:
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.categories));
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.btn_wishlist:
                        if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.LOG_OUT, true)) {
                            showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_add_into_fav), "OK!",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(MainActivity.this, Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        } else {
                            rl_header.setVisibility(View.VISIBLE);
                            title.setText(getResources().getString(R.string.whislist));
                            viewPager.setCurrentItem(3);
                        }
                        break;
                    case R.id.btn_account:
                        if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.LOG_OUT, true)) {
                            showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_profile), "OK!",
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(MainActivity.this, Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        } else {
                            rl_header.setVisibility(View.GONE);
                            title.setText(getResources().getString(R.string.account));
                            viewPager.setCurrentItem(4);
                        }
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    rl_header.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.home));
                    viewPager.setCurrentItem(0);
                } else if (position == 1) {
                    rl_header.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.top_pics));
                    viewPager.setCurrentItem(1);
                } else if (position == 2) {
                    rl_header.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.categories));
                    viewPager.setCurrentItem(2);
                } else if (position == 3) {
                    if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.LOG_OUT, true)) {
                        showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_add_into_fav), "OK!",
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(MainActivity.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    } else {
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.whislist));
                        viewPager.setCurrentItem(3);
                    }
                } else if (position == 4) {
                    if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.LOG_OUT, true)) {
                        showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_profile), "OK!",
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(MainActivity.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    } else {
                        rl_header.setVisibility(View.GONE);
                        title.setText(getResources().getString(R.string.account));
                        viewPager.setCurrentItem(4);
                    }
                }

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d(getString(R.string.page), getString(R.string.on_page_selected) + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void initializeUI() {
        logout = findViewById(R.id.logout);
        text_chat = (TextView) findViewById(R.id.text_chat);
        help_center = findViewById(R.id.help_center);
        rl_header = (Toolbar) findViewById(R.id.header);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        navigation_icon.setImageResource(R.drawable.menu_icon);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        navi_menu = (RecyclerView) findViewById(R.id.menu_items);
        navmyorder = findViewById(R.id.navmyorder);
        navmyaddress = findViewById(R.id.navmyaddress);
        navmywishlist = findViewById(R.id.navmywishlist);
        headerView = findViewById(R.id.headerView);
        language_change = findViewById(R.id.language_change);
        Currency_changer = findViewById(R.id.Currency_changer);
        tv_notifications = findViewById(R.id.tv_notifications);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        navi_menu.setLayoutManager(layoutManager);
    }

    private void initializeValues() {
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        title.setText(getResources().getString(R.string.home));

        navigation_icon.getLayoutParams().height = 70;
        navigation_icon.getLayoutParams().width = 70;
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
        setupViewPager(viewPager);
        logout.setOnClickListener(this);
        help_center.setOnClickListener(this);
        navmyorder.setOnClickListener(this);
        navmyaddress.setOnClickListener(this);
        navmywishlist.setOnClickListener(this);
        headerView.setOnClickListener(this);
        language_change.setOnClickListener(this);
        Currency_changer.setOnClickListener(this);
        tv_notifications.setOnClickListener(this);
        text_chat.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Home home = new Home();
        TopPicsFragment topPicsFragment = new TopPicsFragment();
        CategoryScreen categoryScreen = new CategoryScreen();
        Whishlist whishlist = new Whishlist();
        MyAccount myAccount = new MyAccount();

        adapter.addFragment(home);
        adapter.addFragment(topPicsFragment);
        adapter.addFragment(categoryScreen);
        adapter.addFragment(whishlist);
        adapter.addFragment(myAccount);

        viewPager.setAdapter(adapter);

        if (getIntent().getExtras() != null && getIntent().getStringExtra("Action") != null && getIntent().getStringExtra("Action").equalsIgnoreCase("reset password")) {
            rl_header.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.account));
            viewPager.setCurrentItem(4);
            bottomNavigationView.getMenu().getItem(4).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(4);
        } else if (getIntent().getExtras() != null && getIntent().getStringExtra("Option") != null && getIntent().getStringExtra("Option").equalsIgnoreCase("true")) {
            if (getIntent().getStringExtra("Position").equalsIgnoreCase("0")) {
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.home));
                viewPager.setCurrentItem(0);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(0);
            } else if (getIntent().getStringExtra("Position").equalsIgnoreCase("3")) {
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.whislist));
                viewPager.setCurrentItem(3);
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(3);
            }
        } else {
            rl_header.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.home));
            viewPager.setCurrentItem(0);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(0);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.logout:
                preferenceUtils.saveBoolean(PreferenceUtils.LOG_OUT, true);
                preferenceUtils.logOut();
                String languageToLoad = "ar"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                preferenceUtils.setLanguage("ar");
                intent = new Intent(context, OnBoardingScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
                break;
            case R.id.help_center:
                mDrawerLayout.closeDrawers();
                intent = new Intent(MainActivity.this, HelpCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.navmyorder:
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    mDrawerLayout.closeDrawers();
                    intent = new Intent(MainActivity.this, MyOrder.class);
                    startActivity(intent);
                } else {
                    showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_order), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }

                break;
            case R.id.tv_notifications:
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    mDrawerLayout.closeDrawers();
                    intent = new Intent(MainActivity.this, Notifications.class);
                    startActivity(intent);
                } else {
                    showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_notifications), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
                break;
            case R.id.navmyaddress:
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    mDrawerLayout.closeDrawers();
                    intent = new Intent(MainActivity.this, MyAddressActivity.class);
                    startActivity(intent);
                } else {
                    showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_address_page), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }

                break;
            case R.id.navmywishlist:
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    mDrawerLayout.closeDrawers();
                    viewPager.setCurrentItem(3);
                } else {
                    showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_fav_page), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
                break;
            case R.id.headerView:
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    mDrawerLayout.closeDrawers();
                    viewPager.setCurrentItem(4);
                } else {
                    showDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE, "", getString(R.string.plz_login_to_goto_profile_page), "OK!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
                break;
            case R.id.language_change:
                mDrawerLayout.closeDrawers();
                showLang();
                break;
            case R.id.text_chat:
                mDrawerLayout.closeDrawers();
                Intent intent1 = new Intent(MainActivity.this, ZopimChatActivity.class);
                startActivity(intent1);
                break;
            case R.id.Currency_changer:
                mDrawerLayout.closeDrawers();
                showCurrency();
                break;
        }
    }

    public void showLang() {
        final Dialog dialogLanguage = new Dialog(MainActivity.this);
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLanguage.setContentView(R.layout.dailog_select_language);
        dialogLanguage.setCanceledOnTouchOutside(true);
        dialogLanguage.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialogLanguage.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        final RadioButton rbEnglish = (RadioButton) dialogLanguage.findViewById(R.id.rbEnglish);
        final RadioButton rbArbic = (RadioButton) dialogLanguage.findViewById(R.id.rbArbic);
        Button btnSubmit = (Button) dialogLanguage.findViewById(R.id.btnapply);
        Button btnCancel = (Button) dialogLanguage.findViewById(R.id.btncancel);

        if (strSelectLanguage.equalsIgnoreCase("en")) {
            rbEnglish.setChecked(true);

        } else if (strSelectLanguage.equalsIgnoreCase("ar")) {
            rbArbic.setChecked(true);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbEnglish.isChecked()) {
                    languageToLoad = "en"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setLanguage("en");

                    dialogLanguage.dismiss();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (rbArbic.isChecked()) {
                    languageToLoad = "ar"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setLanguage("ar");

                    dialogLanguage.dismiss();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLanguage.dismiss();
            }
        });
        dialogLanguage.show();

    }

    public void showCurrency() {
        final Dialog dialogCurrency = new Dialog(MainActivity.this);
        dialogCurrency.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCurrency.setContentView(R.layout.dailog_select_currency);
        dialogCurrency.setCanceledOnTouchOutside(true);
        dialogCurrency.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialogCurrency.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        final RadioButton rbSAR = (RadioButton) dialogCurrency.findViewById(R.id.rbSAR);
        final RadioButton rbUSD = (RadioButton) dialogCurrency.findViewById(R.id.rbUSD);
        final RadioButton rbKWD = (RadioButton) dialogCurrency.findViewById(R.id.rbKWD);
        final RadioButton rbAED = (RadioButton) dialogCurrency.findViewById(R.id.rbAED);
        Button btnSubmit = dialogCurrency.findViewById(R.id.btnapply);
        Button btnCancel = dialogCurrency.findViewById(R.id.btncancel);

        if (strCurrency.equalsIgnoreCase(Constants.SAR)) {
            rbSAR.setChecked(true);
        } else if (strCurrency.equalsIgnoreCase(Constants.AED)) {
            rbAED.setChecked(true);
        } else if (strCurrency.equalsIgnoreCase(Constants.KWD)) {
            rbKWD.setChecked(true);
        } else if (strCurrency.equalsIgnoreCase(Constants.USD)) {
            rbUSD.setChecked(true);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbSAR.isChecked()) {
                    preferenceUtils.setCurrency(Constants.SAR);
                    mDrawerLayout.closeDrawers();
                    recreate();
                    dialogCurrency.dismiss();
                } else if (rbAED.isChecked()) {
                    preferenceUtils.setCurrency(Constants.AED);
                    mDrawerLayout.closeDrawers();
                    recreate();
                    dialogCurrency.dismiss();
                } else if (rbKWD.isChecked()) {
                    preferenceUtils.setCurrency(Constants.KWD);
                    mDrawerLayout.closeDrawers();
                    dialogCurrency.dismiss();
                    recreate();
                } else if (rbUSD.isChecked()) {
                    preferenceUtils.setCurrency(Constants.USD);
                    mDrawerLayout.closeDrawers();
                    recreate();
                    dialogCurrency.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCurrency.dismiss();
            }
        });
        dialogCurrency.show();
    }

    public void home_fragment_service(final Context context) {
        preferenceUtils = new PreferenceUtils(this);
        new_arrival_list = new ArrayList();
        category_list = new ArrayList<>();

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.HOME_FRAGMENT_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

//        final ProgressDialog progressDoalog;
//        progressDoalog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
//        progressDoalog.setCancelable(false);
//        progressDoalog.setMessage("Loading....");
//        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDoalog.show();

        callRetrofit.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

//                progressDoalog.dismiss();
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
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");

                        if (status == 1) {
                            JSONObject data_Object = lObj.getJSONObject("data");
                            String base_path = lObj.getString("base_path");
                            JSONArray cat_array = data_Object.getJSONArray("categories");
                            category_list_array = new ArrayList<>();
                            Category_Model model = new Category_Model("-1", getResources().getString(R.string.all_categories), "");
                            category_list_array.add(model);
                            for (int i = 0; i < cat_array.length(); i++) {
                                JSONObject cat_object = cat_array.getJSONObject(i);
                                Category_Model category_model = new Category_Model();
                                category_model.setCat_id(cat_object.getString("cat_id"));
                                category_model.setCname(cat_object.getString("cname"));
                                category_model.setCat_image(base_path + cat_object.getString("cat_image"));

                                category_list.add(category_model);
                                category_list_array.add(category_model);
                            }

                            nav_menu_item_adapter item_adapter = new nav_menu_item_adapter(context, category_list);
                            navi_menu.setAdapter(item_adapter);
                            navi_menu.setNestedScrollingEnabled(false);


                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }

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