package edu.nyu.switchkey;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

/**
 * Created by hrk19_000 on 11/25/2015.
 */
public class ConnectionUI extends AppCompatActivity {
    public FragmentManager mFragmentManager;
    static SharedPreferences connect;
    static SharedPreferences.Editor editor;
    final static private String APP_KEY = "zwjlvheizt8v6z4";
    final static private String APP_SECRET = "zzh8eeiy0ysonji";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    public static DropboxAPI<AndroidAuthSession> dropbox;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);
        mFragmentManager = getSupportFragmentManager();
        CompoundButton Gallerybutton, Calendarbutton, Camerabutton, Dropboxbutton;
        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(APP_KEY, APP_SECRET);
        session = new AndroidAuthSession(pair);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        Gallerybutton = (CompoundButton) findViewById(R.id.switch1);
        Calendarbutton = (CompoundButton) findViewById(R.id.switch2);
        Camerabutton = (CompoundButton) findViewById(R.id.switch3);
        Dropboxbutton = (CompoundButton) findViewById(R.id.switch4);

        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
                startActivityForResult(intent, FacebookLoginActivity.FACEBOOK);
            }
        });

        Calendarbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                storePref(application.SSP_CALENDAR, isChecked);
            }
        });


        Camerabutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                storePref(application.SSP_CAMERA, isChecked);
            }
        });

        Dropboxbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dropbox.getSession().startOAuth2Authentication(ConnectionUI.this);
                } else {
                    dropbox.getSession().unlink();
                }

                storePref(application.SSP_DROPBOX, isChecked);
            }
        });

        Gallerybutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                storePref(application.SSP_GALLERY, isChecked);
            }
        });
    }

    public void storePref(String prefKey, boolean isChecked) {
        connect = application.getSharedPrefrences();
        editor = connect.edit();
        editor.remove(prefKey).apply();
        editor.putString(prefKey, Boolean.toString(isChecked)).apply();

        Toast.makeText(this, "Preference successfully saved" + Boolean.toString(isChecked), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        //setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dropbox.getSession().authenticationSuccessful()) {
            try {
                dropbox.getSession().finishAuthentication();
                String accessToken = dropbox.getSession().getOAuth2AccessToken();
                SharedPreferences pref = getSharedPreferences(DROPBOX_NAME, 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("token", accessToken);
                editor.apply();
                //loggedIn(true);
            } catch (IllegalStateException e) {
                Log.i("DbAuthlog", "Error", e);
            }
        }

    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("##FACEBOOK", "in activity result");
        int response = data.getIntExtra("result", 0);
        Log.d("##FACEBOOK", "request code" + requestCode);
        if (response == FacebookLoginActivity.LOGIN_SUCCESS) {
            Log.d("##FACEBOOK", "" + data.getStringExtra("name"));
            Log.d("##FACEBOOK", "" + data.getStringExtra("email"));
            Log.d("##FACEBOOK", "" + data.getStringExtra("token"));
            Log.d("##FACEBOOK", "" + data.getStringExtra("photo"));
        }
    }
}
