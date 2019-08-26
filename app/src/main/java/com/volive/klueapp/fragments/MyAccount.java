package com.volive.klueapp.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.klueapp.activities.MainActivity;
import com.volive.klueapp.activities.ResetPassword;
import com.volive.klueapp.activities.Splash2;
import com.volive.klueapp.R;
import com.volive.klueapp.utils.API_class;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;
import com.volive.klueapp.utils.Retrofit_funtion_class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MyAccount extends Fragment implements View.OnClickListener {
    public static final int MY_REQUEST_CODE = 2;
    final int PICK_IMAGE = 2;
    final int CAMERA_CAPTURE = 1;
    public String PickedImgPath = null;
    Toolbar toolbar;
    ImageView backarrow, profile_add;
    CircleImageView profile_image;
    EditText et_mobile, et_email;
    File file = null;
    Button saveaddress, reset_password;
    TextView tv_name, tv_mail;
    PreferenceUtils preferenceUtils;
    String fname, user_id, mobile, st_profile_image, st_email;
    String st_country_code;
    RelativeLayout profile_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account, container, false);
        preferenceUtils = new PreferenceUtils(getActivity());
        st_country_code=preferenceUtils.getStringFromPreference(PreferenceUtils.Country_Code,"");
        initializeUI(view);
        initializeValues(view);

        Glide.with(getActivity()).load(preferenceUtils.getStringFromPreference(PreferenceUtils.BASE_PATH, "") + preferenceUtils.getStringFromPreference(PreferenceUtils.IMAGE, ""))
                .into(profile_image);
        et_email.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.Email, ""));
        et_mobile.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.Mobile, ""));
        tv_name.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.User_name, ""));
        tv_mail.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.Email, ""));

        return view;
    }

    private void initializeUI(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        profile_image = view.findViewById(R.id.profile_image);
        tv_name = view.findViewById(R.id.profile_name);
        tv_mail = view.findViewById(R.id.profile_email);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_email = view.findViewById(R.id.et_email);
        reset_password = view.findViewById(R.id.reset_password);
        profile_add = view.findViewById(R.id.profile_add);
        saveaddress = (Button) view.findViewById(R.id.saveaddress);
        reset_password = view.findViewById(R.id.reset_password);
        backarrow = (ImageView) view.findViewById(R.id.backarrow);
        profile_layout=view.findViewById(R.id.profile_layout);
    }

    private void initializeValues(View view) {
        reset_password.setOnClickListener(this);
        backarrow.setOnClickListener(this);
        saveaddress.setOnClickListener(this);
        profile_layout.setOnClickListener(this);

    }

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

    private void selectImage() {

        final CharSequence[] options = {getResources().getString(R.string.takephoto), getResources().getString(R.string.choosefrmgallery), getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.addphoto));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (Objects.equals(options[item], getResources().getString(R.string.takephoto))) {
                    cameraIntent();

                } else if (Objects.equals(options[item], getResources().getString(R.string.choosefrmgallery))) {
                    browse();

                } else if (Objects.equals(options[item], getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void browse() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK) {

                onCaptureImageResult(data);

            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {

                Uri picUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(picUri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                PickedImgPath = c.getString(columnIndex);

                try {
                    Bitmap bm = BitmapFactory.decodeStream(
                            getActivity().getContentResolver().openInputStream(picUri));
                    profile_image.setImageBitmap(bm);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Log.d("Selected Image path: ", PickedImgPath);
                c.close();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            PickedImgPath = destination.getAbsolutePath();
            Log.e("Camera Path", destination.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profile_image.setImageBitmap(thumbnail);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.reset_password:
                intent = new Intent(getActivity(), ResetPassword.class);
                startActivity(intent);
//                getActivity().finish();
                break;
            case R.id.saveaddress:
                fname = tv_name.getText().toString().trim();
                st_email = et_email.getText().toString().trim();
                mobile = et_mobile.getText().toString().trim();

                if (!Objects.equals(st_email, "") && !Objects.equals(fname, "") && !Objects.equals(mobile, "") && !Objects.equals(PickedImgPath, "")) {
                    update_profile_service();
                } else {

                }
                break;
            case R.id.profile_layout:
                if (!Splash2.checkPermissions(getActivity())) {

                } else {
                    selectImage();
                }
                break;
            case R.id.backarrow:
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().onBackPressed();
                break;
        }
    }

    public void update_profile_service() {
        MultipartBody.Part image_profile = null;
        if (PickedImgPath != null) {
            file = new File(PickedImgPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse(getMimeType(PickedImgPath)), file);
            image_profile = MultipartBody.Part.createFormData("profile_image", file.getName(), requestBody);
            Log.d("Image", ">>>>>>>>>>" + image_profile);
        }

        RequestBody r_lang = RequestBody.create(MediaType.parse("multipart/form-data"), preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));
        RequestBody r_name = RequestBody.create(MediaType.parse("multipart/form-data"), fname);
        RequestBody r_mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody r_api_key = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.API_KEY);
        RequestBody r_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));
        RequestBody r_email = RequestBody.create(MediaType.parse("multipart/form-data"), st_email);

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.UPDATE_PROFILE(r_api_key, r_lang, r_user_id, r_name, r_email, r_mobile, image_profile);

        final ProgressDialog progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    Log.d("Regestration", "response  >>" + searchResponse.toString());

                    try {
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");
                        if (status == 1) {
                            JSONObject jsonObject = lObj.getJSONObject("user_info");

                            user_id = jsonObject.getString("user_id");
                            fname = jsonObject.getString("fname");
                            st_email = jsonObject.getString("email");
                            mobile = jsonObject.getString("mobile");
                            st_profile_image = jsonObject.getString("image");

                            preferenceUtils.saveString(PreferenceUtils.User_name, fname);
                            preferenceUtils.saveString(PreferenceUtils.USER_ID, user_id);
                            preferenceUtils.saveString(PreferenceUtils.Email, st_email);
                            preferenceUtils.saveString(PreferenceUtils.Mobile, mobile);
                            preferenceUtils.saveString(PreferenceUtils.IMAGE, st_profile_image);
                        }
                        String base_url = lObj.getString("base_path");

                        preferenceUtils.saveString(PreferenceUtils.IMAGE, st_profile_image);

                        if (!Objects.equals(base_url, "")) {
                            Glide.with(MyAccount.this)
                                    .load(base_url + st_profile_image)
                                    .into(profile_image);
                        }
                        tv_mail.setText(st_email);

                    } catch (JSONException e) {
                        Log.e("error", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDoalog.dismiss();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });

    }

}