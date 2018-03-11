package ivan.mamula.creitive.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import ivan.mamula.creitive.R;
import ivan.mamula.creitive.Utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent showNextScreenIntent;
                if(Constants.getToken(getApplicationContext())==null) {
                    showNextScreenIntent = new Intent(SplashScreenActivity.this,
                            LoginActivity.class);
                }
                else
                {
                    showNextScreenIntent = new Intent(SplashScreenActivity.this,
                            BlogsListActivity.class);
                }
                startActivity(showNextScreenIntent);
                finish();
            }
        };
        handler.postDelayed(runnable, SPLASH_SCREEN_DURATION);
    }
}
