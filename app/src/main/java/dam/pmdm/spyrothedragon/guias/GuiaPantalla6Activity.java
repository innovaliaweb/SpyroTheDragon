package dam.pmdm.spyrothedragon.guias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;

public class GuiaPantalla6Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guia_pantalla6);

        Button btnFinalizar = findViewById(R.id.btnFinalizarGuia);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marcarGuiaComoVista();
                startActivity(new Intent(GuiaPantalla6Activity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void marcarGuiaComoVista() {
        SharedPreferences prefs = getSharedPreferences("GuiaSpyro", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("guiaCompletada", true);
        editor.apply();
    }
}

