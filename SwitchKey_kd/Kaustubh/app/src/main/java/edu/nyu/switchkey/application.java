package edu.nyu.switchkey;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hrk19_000 on 11/28/2015.
 */
public class application extends Application {
    public static SharedPreferences sharedPreferences;
    //used for passing data references to keyboard
    public static String SP_GALLERY = "galleryOp";
    public static String SP_DROPBOX = "dropboxOp";
    public static String CALLED_FOR_RESULT = "getResult";
    // used to store user choice for connected apps
    public static String SSP_GALLERY = "gallery";
    public static String SSP_CALENDAR = "calendar";
    public static String SSP_CAMERA = "camera";
    public static String SSP_FACEBOOK = "facebook";
    public static String SSP_DROPBOX = "dropbox";


    public static SharedPreferences getSharedPrefrences(){
        return sharedPreferences;
    }
    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate();
        printHash();
    }


    public void printHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "edu.nyu.switchkey",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("nyu.edu my hash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
