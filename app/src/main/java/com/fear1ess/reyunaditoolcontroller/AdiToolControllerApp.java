package com.fear1ess.reyunaditoolcontroller;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.fear1ess.reyunaditoolcontroller.cmd.WSConnectCmd;
import com.fear1ess.reyunaditoolcontroller.thread.WSSendDataProceduce;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class AdiToolControllerApp extends Application {
    private static Context mAppContext = null;
    private static int maxDeviceNum;

    private static MainUIHandler uiHandler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        synchronized (this){
            if(mAppContext == null) mAppContext = base;
        }
        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            maxDeviceNum = ai.metaData.getInt("max_device_num");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uiHandler = new MainUIHandler();
    }

    public static MainUIHandler getUiHandler(){
        return uiHandler;
    }

    public static Context getAppContext(){
        return mAppContext;
    }

    public static int getMaxDeviceNum() {
        return maxDeviceNum;
    }

    public static class TrustAllHostnamesVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    public static class TrustAllCertificateManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}
