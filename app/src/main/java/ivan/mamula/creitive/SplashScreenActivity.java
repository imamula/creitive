package ivan.mamula.creitive;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_DURATION=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        Handler handler= new Handler();
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                Intent showLoginIntent= new Intent(SplashScreenActivity.this,LoginActivity.class);
                startActivity(showLoginIntent);
                finish();
            }
        };
        handler.postDelayed(runnable,SPLASH_SCREEN_DURATION);
    }
}
