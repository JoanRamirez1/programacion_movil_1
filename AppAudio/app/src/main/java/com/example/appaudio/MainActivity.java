package com.example.appaudio;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnAveAzul, btnTucan, btnPaloma;
    private MediaPlayer reproductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAveAzul = findViewById(R.id.btnAveAzul);
        btnTucan = findViewById(R.id.btnTucan);
        btnPaloma = findViewById(R.id.btnPaloma);

        btnAveAzul.setOnClickListener(v -> reproducirSonido(R.raw.aveazul));
        btnTucan.setOnClickListener(v -> reproducirSonido(R.raw.tucan));
        btnPaloma.setOnClickListener(v -> reproducirSonido(R.raw.paloma));
    }

    private void reproducirSonido(int sonido) {
        if (reproductor != null) {
            reproductor.release();
            reproductor = null;
        }

        reproductor = MediaPlayer.create(this, sonido);

        if (reproductor != null) {
            reproductor.setOnCompletionListener(mp -> {
                mp.release();
                if (reproductor == mp) {
                    reproductor = null;
                }
            });
            reproductor.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reproductor != null) {
            reproductor.release();
            reproductor = null;
        }
    }
}