package dam.pmdm.spyrothedragon.guias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;

public class GuiaPantalla3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guia_overlay_mundos);

        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        Button btnOmitir = findViewById(R.id.btnOmitir);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuiaPantalla3Activity.this, GuiaPantalla4Activity.class);
                startActivity(intent);
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