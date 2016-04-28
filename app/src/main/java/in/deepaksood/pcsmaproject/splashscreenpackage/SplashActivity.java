package in.deepaksood.pcsmaproject.splashscreenpackage;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.deepaksood.pcsmaproject.loginpackage.LoginActivity;
import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.mainactivitypackage.MainActivity;
import in.deepaksood.pcsmaproject.preferencemanagerpackage.PrefManager;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PrefManager prefManager = new PrefManager(this);
        if(prefManager.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
