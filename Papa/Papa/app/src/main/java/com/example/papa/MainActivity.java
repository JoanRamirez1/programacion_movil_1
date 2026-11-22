package com.example.papa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.content.ClipData;
import android.view.View;
import android.view.DragEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
    FrameLayout escenario;
    ImageView piezaDisponible;
    Spinner selector;
    Button btnPosiciones;
    TextView resultado;
    int piezaActual = 0;

    // Tipos de pieza (coinciden con la posición en el Spinner)
    static final int OJOS = 1;
    static final int NARIZ = 2;
    static final int SOMBRERO = 3;
    static final int PIES = 4;

    // Todo se guarda por tipo: el índice 0 no se usa
    ImageView[] colocadas = new ImageView[5];
    float[] posX = {-1, -1, -1, -1, -1};
    float[] posY = {-1, -1, -1, -1, -1};

    int[] imagenes;
    // Tamaño de cada pieza en dp (ancho y alto)
    int[] anchoDp = {0, 72, 72, 120, 130};
    int[] altoDp = {0, 72, 72, 78, 84};

    String[] nombres = {"", "OJOS", "NARIZ", "SOMBRERO", "PIES"};
    String[] noColocada = {
            "",
            "Ojos: No colocados",
            "Nariz: No colocada",
            "Sombrero: No colocado",
            "Pies: No colocados"
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        escenario = findViewById(R.id.escenario);
        piezaDisponible = findViewById(R.id.piezadisponible);
        selector = findViewById(R.id.selector);
        btnPosiciones = findViewById(R.id.btnposiciones);
        resultado = findViewById(R.id.resultado);

        imagenes = new int[]{
                0,
                R.drawable.ojos,
                R.drawable.nariz,
                R.drawable.sombreropapa,
                R.drawable.piespapa
        };

        String[] opciones = {
                getString(R.string.selecciontexto),
                getString(R.string.opojos),
                getString(R.string.opnariz),
                getString(R.string.opsombrero),
                getString(R.string.oppies)
        };

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        opciones
                );

        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        selector.setAdapter(adaptador);

        selector.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        piezaActual = position;
                        piezaDisponible.setVisibility(View.GONE);

                        // position 1..4 = ojos, nariz, sombrero, pies
                        if (position >= OJOS && position <= PIES) {
                            // Si ya había una colocada, se quita para poner la nueva
                            if (colocadas[position] != null) {
                                escenario.removeView(colocadas[position]);
                                colocadas[position] = null;
                                posX[position] = -1;
                                posY[position] = -1;
                            }
                            piezaDisponible.setImageResource(imagenes[position]);
                            piezaDisponible.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        piezaDisponible.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, android.view.MotionEvent event) {

                        if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {

                            if (piezaActual == 0) {
                                return false;
                            }
                            ClipData datos = ClipData.newPlainText(
                                    "pieza",
                                    String.valueOf(piezaActual)
                            );
                            View.DragShadowBuilder sombra =
                                    new View.DragShadowBuilder(v);
                            v.startDrag(
                                    datos,
                                    sombra,
                                    Integer.valueOf(piezaActual),
                                    0
                            );
                            return true;
                        }
                        return true;
                    }
                });

        escenario.setOnDragListener(
                new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                return event.getLocalState() instanceof Integer;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                return true;
                            case DragEvent.ACTION_DRAG_LOCATION:
                                return true;
                            case DragEvent.ACTION_DRAG_EXITED:
                                return true;
                            case DragEvent.ACTION_DROP:
                                int tipo = (Integer) event.getLocalState();
                                colocarPieza(
                                        event.getX(),
                                        event.getY(),
                                        tipo
                                );
                                return true;
                            case DragEvent.ACTION_DRAG_ENDED:
                                return true;
                        }
                        return false;
                    }
                });

        btnPosiciones.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String texto = "";
                        for (int tipo = OJOS; tipo <= PIES; tipo++) {
                            if (colocadas[tipo] != null) {
                                texto += nombres[tipo] + "\n";
                                texto += "X: " + Math.round(posX[tipo]) + "\n";
                                texto += "Y: " + Math.round(posY[tipo]) + "\n\n";
                            } else {
                                texto += noColocada[tipo] + "\n\n";
                            }
                        }
                        resultado.setText(texto.trim());
                    }
                });
    }

    private void colocarPieza(float x, float y, int tipo) {
        ImageView nuevaPieza = new ImageView(this);
        nuevaPieza.setImageResource(imagenes[tipo]);
        nuevaPieza.setScaleType(ImageView.ScaleType.FIT_CENTER);

        int anchoPieza = dp(anchoDp[tipo]);
        int altoPieza = dp(altoDp[tipo]);

        int anchoEscenario = escenario.getWidth();
        int altoEscenario = escenario.getHeight();

        int izquierda = Math.max(
                0,
                Math.min(
                        (int) x - anchoPieza / 2,
                        anchoEscenario - anchoPieza
                )
        );

        int arriba = Math.max(
                0,
                Math.min(
                        (int) y - altoPieza / 2,
                        altoEscenario - altoPieza
                )
        );

        FrameLayout.LayoutParams parametros =
                new FrameLayout.LayoutParams(
                        anchoPieza,
                        altoPieza
                );

        parametros.leftMargin = izquierda;
        parametros.topMargin = arriba;
        escenario.addView(nuevaPieza, parametros);

        float factor = 500f / anchoEscenario;
        float centroX = izquierda + anchoPieza / 2f;
        float centroY = arriba + altoPieza / 2f;

        colocadas[tipo] = nuevaPieza;
        posX[tipo] = centroX * factor;
        posY[tipo] = centroY * factor;

        piezaDisponible.setVisibility(View.GONE);
    }

    private int dp(float valor) {
        return (int) (
                valor *
                        getResources().getDisplayMetrics().density
                        + 0.5f
        );
    }
}