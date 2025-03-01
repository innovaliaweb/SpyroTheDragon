package dam.pmdm.spyrothedragon.guias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;

public class GuiaPantalla5Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guia_overlay_info); // Pantalla superpuesta para el icono de Información

        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        Button btnOmitir = findViewById(R.id.btnOmitir);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuiaPantalla5Activity.this, GuiaPantalla6Activity.class));
                finish();
            }
        });

        btnOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarGuia();
            }
        });
    }

    private void finalizarGuia() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
