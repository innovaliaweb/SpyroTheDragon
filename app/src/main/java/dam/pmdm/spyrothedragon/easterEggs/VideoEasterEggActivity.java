package dam.pmdm.spyrothedragon.easterEggs;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.R;

public class VideoEasterEggActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configurar pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_video_easter_egg);
        
        // Iniciar reproducción de vídeo
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.spyro_video);
        
        // Cuando el vídeo termine, volver a la pantalla anterior
        videoView.setOnCompletionListener(mp -> finish());
        
        // Iniciar la reproducción
        videoView.start();
    }
}
