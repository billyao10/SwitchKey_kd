package edu.nyu.switchkey;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hrk19_000 on 10/30/2015.
 */
public class myKeyBoard extends InputMethodService
implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard KBQuerty, KBSymbol, mCurKeyboard;
    private EmojiKeyboard mQwertyKeyboard;
    private EmojiKeyboard mSymbolsKeyboard;
    private EmojiKeyboard mSymbolsShiftedKeyboard;
    private static final int  EMOTICON = 300;
    private static final int  CAMERA = 301;
    private static final int  GALLERY = 302;
    private static final int  CALENDER = 303;
    private static final int  DROPBOX = 304;
    private static final int  FACEBOOK = 305;
    private static final int  CHANGEKEYBOARD = 307;
    static final int KEYCODE_EMOJI_1 = -21;
    static final int KEYCODE_EMOJI_2 = -31;
    static final int KEYCODE_EMOJI_3 = -41;
    static final int KEYCODE_EMOJI_4 = -51;
    static final int KEYCODE_EMOJI_5 = -61;

    String TAG;

    private static final String WARNING_MSG = "Application currently not linked";
    private boolean caps = false;

    private int mLastDisplayWidth;


    private EmojiKeyboard mCurrentKeyboard;
    private EmojiKeyboard mEmojiKeyboarda1;
    private EmojiKeyboard mEmojiKeyboarda2;
    private EmojiKeyboard mEmojiKeyboarda3;
    private EmojiKeyboard mEmojiKeyboarda4;
    private EmojiKeyboard mEmojiKeyboardb1;
    private EmojiKeyboard mEmojiKeyboardb2;
    private EmojiKeyboard mEmojiKeyboardc1;
    private EmojiKeyboard mEmojiKeyboardc2;
    private EmojiKeyboard mEmojiKeyboardc3;
    private EmojiKeyboard mEmojiKeyboardc4;
    private EmojiKeyboard mEmojiKeyboardc5;
    private EmojiKeyboard mEmojiKeyboardd1;
    private EmojiKeyboard mEmojiKeyboardd2;
    private EmojiKeyboard mEmojiKeyboardd3;
    private EmojiKeyboard mEmojiKeyboarde1;
    private EmojiKeyboard mEmojiKeyboarde2;
    private EmojiKeyboard mEmojiKeyboarde3;
    private EmojiKeyboard mEmojiKeyboarde4;

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        InputConnection ic = getCurrentInputConnection();
        SharedPreferences SP = application.getSharedPrefrences();
        String value = SP.getString(application.CALLED_FOR_RESULT,null);
        if (value != null)
        {
            ic.commitText(value, 1);
            SharedPreferences.Editor e = SP.edit();
            e.remove(application.CALLED_FOR_RESULT);
            e.apply();
        }

    }

    @Override
    public View onCreateInputView() {

        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        KBQuerty = new Keyboard(this, R.xml.qwerty);
        KBSymbol = new Keyboard(this, R.xml.symbols);
        mCurKeyboard = KBQuerty;
        kv.setKeyboard(mCurKeyboard);
        kv.setOnKeyboardActionListener(this);
 /*     */
        return kv;

    }


    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        String value;
        SharedPreferences SP = application.getSharedPrefrences();
        SharedPreferences.Editor e = SP.edit();

        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;

            case Keyboard.KEYCODE_MODE_CHANGE:
                if (mCurKeyboard == KBQuerty)
                {
                    mCurKeyboard = KBSymbol;
                    kv.setKeyboard(mCurKeyboard);
                }
                else
                {
                    mCurKeyboard = KBQuerty;
                    kv.setKeyboard(mCurKeyboard);
                }
                break;

            case Keyboard.KEYCODE_SHIFT:

                caps = !caps;
                KBQuerty.setShifted(caps);
                kv.invalidateAllKeys();
                break;

            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;

            case CAMERA:
                value = SP.getString(application.SSP_CAMERA,null);
                if (value == null || value.equals("false")){
                    Toast.makeText(this, WARNING_MSG, Toast.LENGTH_SHORT).show();

                }
                else {

                    Intent camintent = new Intent();
                    camintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    camintent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    camintent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(camintent);
                }
                break;

            case DROPBOX:
                value = SP.getString(application.SSP_DROPBOX,null);
                if (value == null || value.equals("false")){
                    Toast.makeText(this, WARNING_MSG, Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    Intent DropboxInt = new Intent(this, Dropbox.class);
                    // Start the Intent
                    DropboxInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(DropboxInt);

                    value = SP.getString(application.SP_DROPBOX,null);
                    if (value == null )
                    {
                        //ic.commitText("Nothing returned from gallery", 1);
                        Toast.makeText(this, "You haven't selected any file", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ic.commitText(value, 1);
                        e.remove(application.SP_GALLERY);
                        e.commit();
                    }

                }
                break;

            case CALENDER:

                value = SP.getString(application.SSP_CALENDAR,null);
                if (value == null || value.equals("false")){
                    Toast.makeText(this, WARNING_MSG, Toast.LENGTH_SHORT).show();
                    break;
                }else {

                    Calendar cal = new GregorianCalendar();
                    cal.setTime(new Date());
                    cal.add(Calendar.MONTH, 2);
                    long time = cal.getTime().getTime();
                    Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    builder.appendPath(Long.toString(time));
                    Intent calIntent = new Intent(Intent.ACTION_VIEW, builder.build());
                    calIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(calIntent);
                }
                break;

            case FACEBOOK:
                value = SP.getString(application.SSP_FACEBOOK,null);
             /*   if (value == null || value.equals("false") ){
                    Toast.makeText(this, WARNING_MSG, Toast.LENGTH_SHORT).show();

                }else {*/
                    Intent FBintent = new Intent(this, FacebookLoginActivity.class);
                    // Start the Intent
                    FBintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(FBintent);
                //}
                break;

            case GALLERY:
                value = SP.getString(application.SSP_GALLERY,null);
                if (value == null || value.equals("false")){
                    Toast.makeText(this, WARNING_MSG, Toast.LENGTH_SHORT).show();

                }else {
                    Intent galleryIntent = new Intent(this, gallery.class);
                    galleryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //galleryIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );
                    this.startActivity(galleryIntent);

                    e.remove(application.CALLED_FOR_RESULT);
                    e.apply();
                   /* if (value == null )
                    {
                        //ic.commitText("Nothing returned from gallery", 1);
                        Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ic.commitText(value, 1);
                        e.remove(application.SP_GALLERY);
                        e.commit();
                    }*/
                }
                break;

            case EMOTICON:
                Log.d("Main", "Primary Code: " + primaryCode);
                mCurKeyboard = mEmojiKeyboarda1;
                kv.setKeyboard(mCurKeyboard);
               /*if (primaryCode == KEYCODE_EMOJI_1 && this.kv != null) {
                    this.changeEmojiKeyboard(new EmojiKeyboard[] {
                            this.mEmojiKeyboarda1,
                            this.mEmojiKeyboarda2,
                            this.mEmojiKeyboarda3,
                            this.mEmojiKeyboarda4
                    });
                } else if (primaryCode == KEYCODE_EMOJI_2 && this.kv != null) {
                    this.changeEmojiKeyboard(new EmojiKeyboard[] {
                            this.mEmojiKeyboardb1,
                            this.mEmojiKeyboardb2
                    });
                } else if (primaryCode == KEYCODE_EMOJI_3 && this.kv!= null) {
                    this.changeEmojiKeyboard(new EmojiKeyboard[] {
                            this.mEmojiKeyboardc1,
                            this.mEmojiKeyboardc2,
                            this.mEmojiKeyboardc3,
                            this.mEmojiKeyboardc4,
                            this.mEmojiKeyboardc5
                    });
                } else if (primaryCode == KEYCODE_EMOJI_4 && this.kv != null) {
                    this.changeEmojiKeyboard(new EmojiKeyboard[] {
                            this.mEmojiKeyboardd1,
                            this.mEmojiKeyboardd2,
                            this.mEmojiKeyboardd3
                    });
                } else if (primaryCode == KEYCODE_EMOJI_5 && this.kv != null) {
                    this.changeEmojiKeyboard(new EmojiKeyboard[] {
                            this.mEmojiKeyboarde1,
                            this.mEmojiKeyboarde2,
                            this.mEmojiKeyboarde3,
                            this.mEmojiKeyboarde4
                    });
                }*/
                break;

            case CHANGEKEYBOARD:
                mCurKeyboard = KBQuerty;
                kv.setKeyboard(mCurKeyboard);
                break;

            case KEYCODE_EMOJI_1:
                if(mCurKeyboard == KBQuerty || mCurKeyboard == KBSymbol || mCurKeyboard == null){
                    Log.d(TAG, "useless button");
                }else{
                    this.changeEmojiKeyboard(new EmojiKeyboard[] {
                            this.mEmojiKeyboarda1,
                            this.mEmojiKeyboarda2,
                            this.mEmojiKeyboarda3,
                            this.mEmojiKeyboarda4
                    });
                }

            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }



    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
        this.changeEmojiKeyboard(new EmojiKeyboard[] {
                this.mQwertyKeyboard, this.mSymbolsKeyboard, this.mSymbolsShiftedKeyboard,
                this.mEmojiKeyboarda1, this.mEmojiKeyboarda2, this.mEmojiKeyboarda3, this.mEmojiKeyboarda4,
                this.mEmojiKeyboardb1, this.mEmojiKeyboardb2,
                this.mEmojiKeyboardc1, this.mEmojiKeyboardc2, this.mEmojiKeyboardc3, this.mEmojiKeyboardc4, this.mEmojiKeyboardc5,
                this.mEmojiKeyboardd1, this.mEmojiKeyboardd2, this.mEmojiKeyboardd3,
                this.mEmojiKeyboarde1, this.mEmojiKeyboarde2, this.mEmojiKeyboarde3, this.mEmojiKeyboarde4,
        });
    }

    @Override
    public void swipeRight() {
        this.changeEmojiKeyboardReverse(new EmojiKeyboard[] {
                this.mQwertyKeyboard, this.mSymbolsKeyboard, this.mSymbolsShiftedKeyboard,
                this.mEmojiKeyboarda1, this.mEmojiKeyboarda2, this.mEmojiKeyboarda3, this.mEmojiKeyboarda4,
                this.mEmojiKeyboardb1, this.mEmojiKeyboardb2,
                this.mEmojiKeyboardc1, this.mEmojiKeyboardc2, this.mEmojiKeyboardc3, this.mEmojiKeyboardc4, this.mEmojiKeyboardc5,
                this.mEmojiKeyboardd1, this.mEmojiKeyboardd2, this.mEmojiKeyboardd3,
                this.mEmojiKeyboarde1, this.mEmojiKeyboarde2, this.mEmojiKeyboarde3, this.mEmojiKeyboarde4,
        });
    }

    @Override
    public void swipeUp() {
    }

    public void changeEmojiKeyboard(EmojiKeyboard[] emojiKeyboard) {
        int j = 0;
        for(int i=0; i<emojiKeyboard.length; i++) {
            if (emojiKeyboard[i] == this.kv.getKeyboard()) {
                j = i;
                break;
            }
        }

        if (j + 1 >= emojiKeyboard.length) {
            this.kv.setKeyboard(emojiKeyboard[0]);
        }else{
            this.kv.setKeyboard(emojiKeyboard[j + 1]);
        }
    }

    public void changeEmojiKeyboardReverse(EmojiKeyboard[] emojiKeyboard) {
        int j = emojiKeyboard.length - 1;
        for(int i=emojiKeyboard.length - 1; i>=0; i--) {
            if (emojiKeyboard[i] == this.kv.getKeyboard()) {
                j = i;
                break;
            }
        }

        if (j - 1 < 0) {
            this.kv.setKeyboard(emojiKeyboard[emojiKeyboard.length - 1]);
        }else{
            this.kv.setKeyboard(emojiKeyboard[j - 1]);
        }
    }

    public void onInitializeInterface() {
        if (this.mQwertyKeyboard != null) {
            int displayWidth = getMaxWidth();

            if (displayWidth == mLastDisplayWidth) {
                return;
            }

            mLastDisplayWidth = displayWidth;
        }

        this.mQwertyKeyboard = new EmojiKeyboard(this, R.xml.qwerty);
        this.mSymbolsKeyboard = new EmojiKeyboard(this, R.xml.symbols);
        //this.mSymbolsShiftedKeyboard = new EmojiKeyboard(this, R.xml.symbols_shift);

        this.mEmojiKeyboarda1 = new EmojiKeyboard(this, R.xml.emoji_a1);
        this.mEmojiKeyboarda2 = new EmojiKeyboard(this, R.xml.emoji_a2);
        this.mEmojiKeyboarda3 = new EmojiKeyboard(this, R.xml.emoji_a3);
        this.mEmojiKeyboarda4 = new EmojiKeyboard(this, R.xml.emoji_a4);

        this.mEmojiKeyboardb1 = new EmojiKeyboard(this, R.xml.emoji_b1);
        this.mEmojiKeyboardb2 = new EmojiKeyboard(this, R.xml.emoji_b2);

        this.mEmojiKeyboardc1 = new EmojiKeyboard(this, R.xml.emoji_c1);
        this.mEmojiKeyboardc2 = new EmojiKeyboard(this, R.xml.emoji_c2);
        this.mEmojiKeyboardc3 = new EmojiKeyboard(this, R.xml.emoji_c3);
        this.mEmojiKeyboardc4 = new EmojiKeyboard(this, R.xml.emoji_c4);
        this.mEmojiKeyboardc5 = new EmojiKeyboard(this, R.xml.emoji_c5);

        this.mEmojiKeyboardd1 = new EmojiKeyboard(this, R.xml.emoji_d1);
        this.mEmojiKeyboardd2 = new EmojiKeyboard(this, R.xml.emoji_d2);
        this.mEmojiKeyboardd3 = new EmojiKeyboard(this, R.xml.emoji_d3);

        this.mEmojiKeyboarde1 = new EmojiKeyboard(this, R.xml.emoji_e1);
        this.mEmojiKeyboarde2 = new EmojiKeyboard(this, R.xml.emoji_e2);
        this.mEmojiKeyboarde3 = new EmojiKeyboard(this, R.xml.emoji_e3);
        this.mEmojiKeyboarde4 = new EmojiKeyboard(this, R.xml.emoji_e4);
    }

    private void showOptionsMenu() {
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
    }

}
