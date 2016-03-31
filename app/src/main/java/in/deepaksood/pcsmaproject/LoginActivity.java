package in.deepaksood.pcsmaproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.facebook.FacebookSdk;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.parse.Parse;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 9001;

    private static String PARSE_APPLICATION_ID="pfuT7c39Q9Latnroh9pbMOVX6hJjE5VRYhy9FbU2";
    private static String PARSE_CLIENT_KEY="rqGDJQ512O8MojOEjir3nmjuCuu8GrkBbLa6YImi";

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private String displayName="";
    private String displayEmailId="";
    private String photoUrl="";
    private String coverUrl="https://drive.google.com/uc?id=0B1jHFoEHN0zfek43ajZrMDZSSms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final GraphRequest graphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Profile profile = Profile.getCurrentProfile();
                                if(profile != null) {
                                    photoUrl = profile.getProfilePictureUri(300,300).toString();
                                }

                                JSONObject json = response.getJSONObject();
                                try {
                                    if(json != null){
                                        displayName = json.getString("name");
                                        displayEmailId = json.getString("email");

                                        String cover = json.getString("cover");
                                        JSONObject jsonCover = new JSONObject(cover);
                                        coverUrl = jsonCover.getString("source");

                                        /*String picture = json.getString("picture");
                                        JSONObject jsonPicture = new JSONObject(picture);
                                        String pictureData = jsonPicture.getString("data");
                                        JSONObject jsonPictureUrl = new JSONObject(pictureData);
                                        photoUrl = jsonPictureUrl.getString("url");*/

                                        startLoginActivity();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture,cover");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();




            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "in.deepaksood.pcsmaproject",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        mStatusTextView = (TextView) findViewById(R.id.sign_in_status);

        try {
            Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "parse initialization error. Restart the app", Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Toast.makeText(LoginActivity.this, "sign-in", Toast.LENGTH_SHORT).show();
                signIn();
                break;
            case R.id.sign_out_button:
                Toast.makeText(LoginActivity.this, "sign-out", Toast.LENGTH_SHORT).show();
                signOut();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            displayName = acct.getDisplayName();
            displayEmailId = acct.getEmail();
            photoUrl = acct.getPhotoUrl().toString();


            if(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                Person.Cover.CoverPhoto coverPhoto = null;
                if(person.getCover() != null) {
                        coverPhoto = person.getCover().getCoverPhoto();
                        coverUrl = coverPhoto.getUrl().toString();
                }
                else {
                    coverUrl = "https://drive.google.com/uc?id=0B1jHFoEHN0zfek43ajZrMDZSSms";
                }

            }
            updateUI(true);
            startLoginActivity();
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, Registration.class);
        Bundle bundle = new Bundle();
        bundle.putString("DISPLAY_NAME",displayName);
        bundle.putString("DISPLAY_EMAIL_ID",displayEmailId);
        bundle.putString("PHOTO_URL",photoUrl);
        bundle.putString("COVER_URL",coverUrl);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }


    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]


    @Override
    protected void onResume() {
        super.onResume();
        /*// Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*// Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);*/
    }
}

