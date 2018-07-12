package com.example.victo.arcdc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ARCDCActivity extends AppCompatActivity {

    private MediaPlayer mp;
    //private MediaPlayer musica_fundo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcdc);

        Toolbar toolbar = findViewById(R.id.toolbar_arcdc);
        setSupportActionBar(toolbar);

        /*musica_fundo = MediaPlayer.create(ARCDCActivity.this, R.raw.musica);
        musica_fundo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                musica_fundo.release();
            }
        });
        musica_fundo.start();*/

        Button opcaoJogar = findViewById(R.id.icone_jogar);
        opcaoJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                executaSom(getApplicationContext());

                Intent intentVaiPraJogar = new Intent(ARCDCActivity.this, JogarActivity.class);
                startActivity(intentVaiPraJogar);
            }
        });

        Button opcaoComoJogar = findViewById(R.id.icone_como_jogar);
        opcaoComoJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                executaSom(getApplicationContext());

                Intent intentVaiPraComoJogar = new Intent(ARCDCActivity.this, ComoJogarActivity.class);
                startActivity(intentVaiPraComoJogar);
            }
        });

        Button opcaoSair = findViewById(R.id.icone_sair);
        opcaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ARCDCActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Sair")
                        .setMessage("Você tem certeza?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Não", null).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_arcdc, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_arcdc_som:
                if(!item.isChecked()){
                    item.setChecked(true);
                    //musica_fundo.setVolume(1,1);
                }
            case R.id.menu_arcdc_mute:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //musica_fundo.setVolume(0, 0);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void executaSom(Context context) {
        mp = MediaPlayer.create(context, R.raw.msg);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });
        mp.start();
    }
}


