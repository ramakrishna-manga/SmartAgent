package rk.com.demo_smartagent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {


    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


         new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent mainIntent = new Intent(Splash.this, MainActivity.class);

                    Splash.this.startActivity(mainIntent);

                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);


        }


    }



