package com.volive.klueapp.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NetworkHelper extends Fragment {
    public static final String TAG = "NetworkHelper";
    public static final String CHECK_INTERNET = "network_connection";
    public static SweetAlertDialog sweetAlertDialog;
    AlertDialog mAlertDialog = null;
    private Activity mActivity;
    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(CHECK_INTERNET) && !intent.getBooleanExtra(CHECK_INTERNET, true)) {
                /*showAlertDialog(mActivity, "Internet Connection",
                        "No internet connection available.\n\n" +
                                "Please check your internet connection and try again.");*/
            } else {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                    mAlertDialog = null;
                }
            }
        }
    };


    public NetworkHelper() {
        // Required empty public constructor
    }

    public static NetworkHelper newInstance() {
        return new NetworkHelper();
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        // Here if condition check for wifi and mobile network is available or not.
        // If anyone of them is available or connected then it will return true,
        // otherwise false;

        if (wifi != null && wifi.isConnected()) {
            return true;
        } else if (mobile != null && mobile.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        return false;
    }

    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

//    public void showAlertDialog(Context context, String title, String message) {
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            return; //already showing
//        } else if (mAlertDialog != null) {
//            mAlertDialog.dismiss();
//            mAlertDialog = null;
//        }
//        mAlertDialog = new AlertDialog.Builder(context).create();
//
//        // Setting Dialog Title
//        mAlertDialog.setTitle(title);
//
//        // Setting Dialog Message
//        mAlertDialog.setMessage(message);
//
//        // Setting OK Button
//        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
//                getString(android.R.string.ok),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        mAlertDialog = null;
//                    }
//                });
//
//        // Showing Alert Message
//        mAlertDialog.show();
//    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter iff = new IntentFilter(CHECK_INTERNET);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(onNotice, iff);
        if (!isInternetConnected(mActivity)) {
            /*showAlertDialog(mActivity, "Internet Connection",
                    "No internet connection available.\n\n" +
                            "Please check your internet connection and try again.");*/

            showDialog(getActivity(), SweetAlertDialog.WARNING_TYPE, "", "Please make sure that Internet is connected.", "OK!",
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if (isInternetConnected(getActivity()))
                                getActivity().recreate();
                        }
                    });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public static void showDialog (Context context,int type, String title, String
            message, String confirmText, SweetAlertDialog.OnSweetClickListener listener){
        sweetAlertDialog = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(confirmText)
                .showCancelButton(false)
                .setConfirmClickListener(listener);
        sweetAlertDialog.show();
    }


}