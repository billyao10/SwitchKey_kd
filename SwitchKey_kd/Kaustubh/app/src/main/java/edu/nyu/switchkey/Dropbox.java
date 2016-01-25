package edu.nyu.switchkey;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.dropbox.chooser.android.DbxChooser;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Can on 2015/11/26.
 */
public class Dropbox extends Activity {


    final static private String APP_KEY = "zwjlvheizt8v6z4";
    final static private String APP_SECRET = "zzh8eeiy0ysonji";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String DROPBOX_FILE_DIR = "/Test/";

    private boolean isUserLoggedIn;
    private DbxChooser chooser;
    private DropboxAPI<AndroidAuthSession> dropbox;
    public static ArrayList<String> sharelinks;
    public static ArrayList<String> files;
    String TAG;
    ListView listView;
    TextView textView;
    static final int DBX_CHOOSER_REQUEST = 0;
    String link = null;
    SharedPreferences DropboxSP;
    SharedPreferences.Editor editor;

    //    DropboxDatabaseHelper dropboxDatabaseHelper;
    public static ArrayList<String> arrayList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox);
        dropbox = ConnectionUI.dropbox;
        chooser = new DbxChooser(APP_KEY);
        chooser.forResultType(DbxChooser.ResultType.DIRECT_LINK)
                .launch(Dropbox.this, DBX_CHOOSER_REQUEST);


        /* listView = (ListView)findViewById(R.id.listView);
//        list = (Button)findViewById(R.id.button2);
        //list.setOnClickListener(this);
 //       ListFiles listFiles = new ListFiles(dropbox, DROPBOX_FILE_DIR);

        files = new ArrayList<>();
        sharelinks = new ArrayList<>();
        String shareAddress = null;
        try {
            DropboxAPI.Entry directory = dropbox.metadata(DROPBOX_FILE_DIR, 1000, null, true, null);
            for (DropboxAPI.Entry entry : directory.contents) {
                files.add(entry.fileName());
                //sharelinks.add(getShareURL(dropbox.share(entry.path).url));
                if (!entry.isDir) {
                    DropboxAPI.DropboxLink shareLink = dropbox.share(entry.path);
                    shareAddress = getShareURL(shareLink.url).replaceFirst("https://www", "https://dl");
                    //Log.d(TAG, shareAddress);
                }
            }
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,files);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _id = files.get(position).substring(0, 1);

                String shareME = sharelinks.get(position);

                SharedPreferences gallerySP = application.getSharedPrefrences();
                SharedPreferences.Editor editor = gallerySP.edit();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("DropBox", shareME);
                clipboard.setPrimaryClip(clip);

                editor.putString(application.SP_DROPBOX,shareME);
                editor.commit();
                finish();
                //textView.setText("Link: " + dropboxDatabaseHelper.getLink(_id));
            }
        });

/*        // listFiles.getSharedLink();
        try{
            arrayList = listFiles.execute().get();
            Log.d(TAG,arrayList.toString());
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,arrayList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String _id = arrayList.get(position).substring(0,1);
                    //                           textView.setText("Link: " + dropboxDatabaseHelper.getLink(_id));
                }
            });
        }catch (Exception e){
            Log.d(TAG,"exception");
        }

        textView = (TextView)findViewById(R.id.textView);*/
        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(APP_KEY, APP_SECRET);
        session = new AndroidAuthSession(pair);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

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
                editor.commit();
            } catch (IllegalStateException e) {
                Log.i("DbAuthlog", "Error", e);
            }
        }
    }


    /* @Override
     public void onClick(View view){
         switch (view.getId()){
             case R.id.button2:
                 ListFiles listFiles = new ListFiles(dropbox, DROPBOX_FILE_DIR);
                 // listFiles.getSharedLink();
                 try{
                     final ArrayList<String> arrayList = listFiles.execute().get();
                     Log.d(TAG,arrayList.toString());
                     ArrayAdapter<String> adapter;
                     adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,arrayList);
                     listView.setAdapter(adapter);
                     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                             String _id = arrayList.get(position).substring(0,1);
  //                           textView.setText("Link: " + dropboxDatabaseHelper.getLink(_id));
                         }
                     });
                 }catch (Exception e){
                     Log.d(TAG,"exception");
                 }

         }

     }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                link = result.getLink().toString();
                Log.d(TAG, link);
                SharedPreferences gallerySP = application.getSharedPrefrences();
                SharedPreferences.Editor editor = gallerySP.edit();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("DropBox", link);
                clipboard.setPrimaryClip(clip);

                editor.putString(application.CALLED_FOR_RESULT, link);
                finish();



                // Handle the result
            } else {
                // Failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

    /*protected class ListFiles extends AsyncTask<Void, Void, ArrayList<String>> {

        private DropboxAPI dropboxApi;
        private String path;

        public ListFiles(DropboxAPI dropboxApi, String path) {
            this.dropboxApi = dropboxApi;
            this.path = path;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> files = new ArrayList<String>();
            String shareAddress = null;
            try {
                DropboxAPI.Entry directory = dropboxApi.metadata(path, 20, null, true, null);
                for (DropboxAPI.Entry entry : directory.contents) {
                    files.add(entry.fileName());
                    if (!entry.isDir) {
                        DropboxAPI.DropboxLink shareLink = dropboxApi.share(entry.path);
                        //shareAddress = getShareURL(shareLink.url).replaceFirst("https://www", "https://dl");
                        //Log.d(TAG, shareAddress);
                    }
                }
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            return files;
        }

        protected void getSharedLink(){
            try {
                DropboxAPI.Entry dir = dropboxApi.metadata(path, 1000, null, true, null);
                for (DropboxAPI.Entry entry : dir.contents) {
                    String shareAddress = null;
                    if (!entry.isDir) {
                        DropboxAPI.DropboxLink shareLink = dropboxApi.share(entry.path);
                        shareAddress = getShareURL(shareLink.url).replaceFirst("https://www", "https://dl");
                        Log.d(TAG, shareAddress);
                        //dropboxDatabaseHelper.insertLink(shareAddress);
                    }
                }
            }catch (DropboxException e){
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

        }
    }

    public String getShareURL(String strURL) {
        URLConnection conn = null;
        String redirectedUrl = null;
        try {
            URL inputURL = new URL(strURL);
            conn = inputURL.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();
            System.out.println("Redirected URL: " + conn.getURL());
            redirectedUrl = conn.getURL().toString();
            is.close();

        } catch (MalformedURLException e) {
            Log.d(TAG, "Please input a valid URL");
        } catch (IOException ioe) {
            Log.d(TAG, "Can not connect to the URL");
        }

        return redirectedUrl;
    }

}*/
