package com.example.victo.arcdc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarTelaInicial();
            }
        }, 3000);
    }

    private void mostrarTelaInicial() {
        Intent intent = new Intent(SplashScreenActivity.this, ARCDCActivity.class);
        startActivity(intent);
        finish();
    }

}
