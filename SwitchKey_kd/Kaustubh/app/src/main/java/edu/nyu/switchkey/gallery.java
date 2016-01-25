package edu.nyu.switchkey;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by hrk19_000 on 10/31/2015.
 */
public class gallery extends Activity {

    private static int RESULT_LOAD_IMG = 1;

    String imgDecodableString;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.gallary);

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);

                cursor.close();
                SharedPreferences gallerySP = application.getSharedPrefrences();
                SharedPreferences.Editor editor = gallerySP.edit();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("DropBox", imgDecodableString);
                clipboard.setPrimaryClip(clip);
                editor.putString(application.CALLED_FOR_RESULT,imgDecodableString);
                editor.apply();

 /*             Intent smsIntent = new Intent(Intent.ACTION_SEND);
                smsIntent.setType("image/png");
                smsIntent.putExtra(Intent.EXTRA_STREAM,selectedImage);
                startActivity(smsIntent.createChooser(smsIntent,"Choose an application"));

//               ImageView imgView = (ImageView) findViewById(R.id.imgView);
//               imgView.setImageBitmap(BitmapFactory.decocddeFile(imgDecodableString));
*/

            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        finally {
            finish();
        }


    }

}
