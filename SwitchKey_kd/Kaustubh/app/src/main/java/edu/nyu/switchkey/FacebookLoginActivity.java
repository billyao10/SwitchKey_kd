package edu.nyu.switchkey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.nyu.switchkey.models.FacebookPhoto;


/**
 * This is core social login activity
 */
public final class FacebookLoginActivity extends AppCompatActivity {
    private static final String TAG = "##SocialLoginActivity##";
    private static final int PERMISSIONS_REQUEST_GET_ACCOUNTS = 1;

    private AtomicBoolean mProcessingFb = new AtomicBoolean(false);
    public static final int FACEBOOK = 2;
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAILURE = 0;
    //Request code used to invoke sign in user interactions.
    private ProgressDialog mProgressDialog;
    private ArrayList<FacebookPhoto> photoList;
    private UiLifecycleHelper mUIHelper;
    private Handler mHandler;
    private Session.StatusCallback mCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_images);
        mHandler = new Handler();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);

        mUIHelper = new UiLifecycleHelper(this, mCallback);
        mUIHelper.onCreate(savedInstanceState);
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("email"))
                    .setCallback(mCallback));
        } else {
            Session.openActiveSession(this, true, mCallback);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        mUIHelper.onResume();

    }

    private void onSessionStateChange(final Session session, final SessionState state,
                                      final Exception exception) {
        Log.d(TAG, "state changed" + state.isOpened() + state.isClosed());
        if (exception != null) {
            Log.d(TAG, "Exception", exception);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", LOGIN_FAILURE);
            Log.e(TAG, "Setting actual1 error");
            setResult(RESULT_OK, resultIntent);
            mProgressDialog.dismiss();
            finish();
            return;
        }
        if (state.isOpened()) {
            Log.i(TAG, "State open");
            if (mProcessingFb.getAndSet(true)) {
                return;
            }
            Log.d(TAG, "Getting my info");
            List<String> permissions = new ArrayList<String>();
            permissions.add("email");
            session.requestNewReadPermissions(new Session.NewPermissionsRequest(this, permissions));


            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            // If the response is successful
                            if (session == Session.getActiveSession()) {

                                if (user != null && user.getProperty("email") != null) {

                                    String email = user.getProperty("email").toString();
                                    String firstName = user.getName().split(" ")[0].trim();
                                    String lastName;
                                    if (user.getName().split(" ").length >= 1) {
                                        lastName = user.getName().split(" ")[1].trim();
                                    }

                                    final String rawResponse = response.getRawResponse();
                                    Log.d(TAG, rawResponse);
                                    photoList = new ArrayList<>();
                                    try {
                                        JSONObject jsonObject = new JSONObject(rawResponse);
                                        JSONObject photos = jsonObject.getJSONObject("photos");
                                        JSONArray list = photos.getJSONArray("data");
                                        for(int i=0; i<list.length(); ++i){


                                            photoList.add(new FacebookPhoto(list.getJSONObject(i).getString("source"),
                                                    list.getJSONObject(i).getLong("id")));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.d(TAG, email);
                                    Log.d(TAG, session.getAccessToken());

                                    Log.d(TAG, "Id " + user.getId());
                                    final Intent resultIntent = new Intent();
                                    resultIntent.putExtra("result", LOGIN_SUCCESS);
                                    resultIntent.putParcelableArrayListExtra("pics", photoList);
                                    resultIntent.putExtra("email", email);
                                    String profilePic =
                                            "https://graph.facebook.com/" +
                                                    user.getId() + "/picture?type=large";
                                    resultIntent.putExtra("token", session.getAccessToken());
                                    resultIntent.putExtra("id", Long.parseLong(user.getId()));
                                    resultIntent.putExtra("name", user.getName());
                                    resultIntent.putExtra("photo", profilePic);
                                    Log.e(TAG, "Setting actual success");
                                    //setResult(LOGIN_SUCCESS, resultIntent);
                                    mProgressDialog.dismiss();
                                    //finish();
                                    setGrid();
                                }
                            }
                            if (response.getError() != null) {
                                // Handle errors, will do so later.
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("result", LOGIN_FAILURE);
                                setResult(RESULT_OK, resultIntent);
                                Log.e(TAG, "Setting actual error");
                                mProgressDialog.dismiss();
                                finish();
                            }
                        }
                    });
            Bundle params = request.getParameters();
            params.putString("fields", "id,email,name,photos.limit(20){source}");
            request.setParameters(params);
            request.executeAsync();
        } else if (state.isClosed()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", LOGIN_FAILURE);
            Log.e(TAG, "Setting actual2 error");
            setResult(RESULT_OK, resultIntent);
            mProgressDialog.dismiss();
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUIHelper.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mUIHelper.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUIHelper.onDestroy();

    }

    private void setGrid() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        if (photoList == null){
            Toast.makeText(this, "No images loaded", Toast.LENGTH_LONG).show();
            return;

        }
        GridAdapter gridAdapter = new GridAdapter();
        gridAdapter.setData(photoList);
        gridView.setAdapter(gridAdapter);
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        mUIHelper.onActivityResult(requestCode, responseCode, intent);

    }
}