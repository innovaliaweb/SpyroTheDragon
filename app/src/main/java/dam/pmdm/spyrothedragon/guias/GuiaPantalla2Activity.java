package dam.pmdm.spyrothedragon.guias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;

public class GuiaPantalla2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guia_overlay_personajes);

        View btnSiguiente = findViewById(R.id.btnSiguiente);
        View btnOmitir = findViewById(R.id.btnOmitir);

        if (btnSiguiente != null) {
            btnSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(GuiaPantalla2Activity.this, GuiaPantalla3Activity.class));
                    finish();
                }
            });
        }

        if (btnOmitir != null) {
            btnOmitir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalizarGuia();
                }
            });
        }
    }

    private void finalizarGuia() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
