package edu.nyu.switchkey;

import android.app.Activity;

/**
 * Created by hrk19_000 on 11/25/2015.
 */
public class myFacebook extends Activity{
    /*private static final String FB_ID = "me";
    private static final String FB_ALBUMS = "albums";
    private static final String FB_PHOTOS = "/photo";
    private static final String FB_DATA = "data";
    private static final String FB_FIELD = "fields";
    String wallAlbumID;
    ArrayList<Bitmap> imageItems = new ArrayList<>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fb_images);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Request request = Request.newMeRequest(session,
                FBFragment.accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        if (object == null) {
                            Log.d("Facebook: ", "Nothing returned");

                            //   setGrid();
                            return;
                        }
                        JSONArray photos = null;
                        try {
                            photos = object.getJSONArray(FB_DATA);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (photos == null) {
                            return;
                        }
                        String link;
                        Bitmap bitmap;
                        for (int i = 0; i < photos.length(); i++) {
                            JSONObject photo = null;
                            try {
                                photo = photos.getJSONObject(i);
                                link = photo.getString("link");
                                bitmap = BitmapFactory.decodeStream((InputStream) new URL(link).getContent());
                                imageItems.add(bitmap);
                            } catch (JSONException | IOException | NullPointerException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        setGrid();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString(FB_FIELD, "id,name,photos.limit(20){link}");
        request.setParameters(parameters);
        request.executeAsync();
        request.setParameters(params);
        request.executeAsync();
    }
    public void setGrid(){
        Toast.makeText(this,"In Set Grid",Toast.LENGTH_LONG).show();
        GridView gridView = (GridView) findViewById(R.id.gridView);
        if (imageItems == null){
            Toast.makeText(this,"No images loaded",Toast.LENGTH_LONG).show();
            return;

        }
        Toast.makeText(this,"Not a empty",Toast.LENGTH_LONG).show();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.fb_grid, imageItems);
        gridView.setAdapter(arrayAdapter);

    }*/
}
