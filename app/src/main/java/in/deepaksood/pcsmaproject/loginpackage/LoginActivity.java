package in.deepaksood.pcsmaproject.loginpackage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
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

import com.facebook.FacebookSdk;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import in.deepaksood.pcsmaproject.R;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private String displayName="";
    private String displayEmailId="";
    private String photoUrl="https://drive.google.com/uc?id=0B1jHFoEHN0zfNXpHbUI2NTd0a1U";
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "sign-in", Toast.LENGTH_SHORT).show();
                signIn();
            }
        });
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
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
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, Registration.class);
        intent.putExtra("DISPLAY_NAME",displayName);
        intent.putExtra("DISPLAY_EMAIL_ID",displayEmailId);
        intent.putExtra("PHOTO_URL",photoUrl);
        intent.putExtra("COVER_URL",coverUrl);
        startActivity(intent);
        finish();
    }
}

