package edu.nyu.switchkey;


/**
 * Created by hrk19_000 on 11/25/2015.
 */
public class FBFragment extends android.support.v4.app.Fragment {
   /* private CallbackManager mCallbackManager;
    public static AccessToken accessToken;
    *//*private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            Log.d("##FB", "Facebook access token " + accessToken.getToken());
            Profile profile = Profile.getCurrentProfile();
            if (profile != null){
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };*//*

    public FBFragment(){


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackManager = CallbackManager.Factory.create();
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState){
        FacebookSdk.sdkInitialize(getActivity());
        mCallbackManager = CallbackManager.Factory.create();
        return inflater.inflate(R.layout.fb_fragment,container,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_photos");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Log.d("##FB2", "Facebook access token " + accessToken.getToken());
                Profile profile = Profile.getCurrentProfile();
                if (profile != null){
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, requestCode, data);
        Log.d("###FB", "onActivity result " + requestCode + "  " + resultCode);
    }*/
}
