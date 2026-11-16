package com.example.galeria;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Galeria extends Activity implements View

        .OnClickListener{
    Button btnAtras, btnAdelante;

    ImageView iv;

    int imagenes[];
    int imagenActual=0;

    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.galeria);

        imagenes = new int[]{
                R.drawable.flash,
                R.drawable.superman,
                R.drawable.mario
        };
        btnAtras = findViewById(R.id.btnatras);
        btnAdelante = findViewById(R.id.btnadelante);
        iv = findViewById(R.id.imagenx);

        btnAdelante.setOnClickListener(this);
        btnAtras.setOnClickListener(this);
    }

    public void onClick(View v){
        if(v.equals(btnAtras)){
            //imagen atras
            if(imagenActual<=0){
                imagenActual=2;
                iv.setImageDrawable(getDrawable(imagenActual));
            }else {
                imagenActual=imagenActual-1;
                iv.setImageDrawable(getDrawable(imagenes[imagenActual]));
            }

        }
        if(v.equals(btnAdelante)){
            //imagen adelante
            if(imagenActual>=2){
                imagenActual=0;
                iv.setImageDrawable(getDrawable(imagenActual));

            }else {
                imagenActual=imagenActual+1;
                iv.setImageDrawable(getDrawable(imagenes[imagenActual]));
            }
        }
    }
}
