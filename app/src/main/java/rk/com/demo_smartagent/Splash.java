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



        if (Build.VERSION.SDK_INT >= 23) {


            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET},
                        1);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant
                System.out.println("allow");

                return;
            } else {
                System.out.println("done");
            }


        }




    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent mainIntent = new Intent(Splash.this,MainActivity.class);

                        mainIntent.putExtra("abc",123);
                        Splash.this.startActivity(mainIntent);

                        overridePendingTransition(R.anim.slide_in_left,
                                R.anim.slide_out_left);
                        Splash.this.finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);


            }
        }



    }

}
