package dam.pmdm.spyrothedragon.easterEggs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class FireAnimationView extends View {

    private Paint paint;
    private ArrayList<Flame> flames;
    private Random random;
    private long startTime;
    
    private ImageView spyroImageView;
    private int[] spyroMouthLocation = new int[2];
    private int startX, startY; // Posición de origen de las llamas

    public FireAnimationView(Context context) {
        super(context);
        init();
    }

    public FireAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        
        flames = new ArrayList<>();
        random = new Random();
        startTime = System.currentTimeMillis();
        
        // Hacer que el fondo sea transparente para que se vea la tarjeta
        setBackgroundColor(Color.TRANSPARENT);
    }
    
    public void setSpyroImageView(ImageView imageView) {
        this.spyroImageView = imageView;
        
        // Calcular la posición de la boca de Spyro (centro-derecha de la imagen)
        if (spyroImageView != null) {
            spyroImageView.getLocationOnScreen(spyroMouthLocation);
            // La boca estaría aproximadamente en el centro-derecha de la imagen
            startX = spyroMouthLocation[0] + spyroImageView.getWidth() * 3 / 4;
            startY = spyroMouthLocation[1] + spyroImageView.getHeight() / 2;
            
            // Convertir coordenadas de pantalla a coordenadas locales de la vista
            int[] myLocation = new int[2];
            getLocationOnScreen(myLocation);
            startX -= myLocation[0];
            startY -= myLocation[1];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // No dibujamos nada hasta que tengamos la referencia a la imagen de Spyro
        if (spyroImageView == null) return;
        
        // Actualizar tiempo
        long elapsed = System.currentTimeMillis() - startTime;
        
        // Añadir nuevas llamas
        if (random.nextFloat() < 0.3f) {
            flames.add(new Flame(startX, startY));
        }
        
        // Dibujar y actualizar llamas
        for (int i = flames.size() - 1; i >= 0; i--) {
            Flame flame = flames.get(i);
            flame.update();
            flame.draw(canvas, paint);
            
            // Eliminar llamas viejas
            if (flame.alpha <= 0) {
                flames.remove(i);
            }
        }
        
        // Continuar la animación
        invalidate();
    }
    
    // Clase interna para representar una llama
    private class Flame {
        float x, y;
        float size;
        int alpha;
        float speedX, speedY;
        int color;
        
        Flame(float startX, float startY) {
            x = startX;
            y = startY;
            size = 10 + random.nextFloat() * 15; // Tamaño más pequeño para que se vea mejor en la tarjeta
            alpha = 255;
            speedX = 2 + random.nextFloat() * 3; // Velocidad más lenta
            speedY = -1 - random.nextFloat() * 2;
            
            // Colores aleatorios entre rojo, naranja y amarillo
            int[] colors = {
                Color.rgb(255, 0, 0),   // Rojo
                Color.rgb(255, 165, 0), // Naranja
                Color.rgb(255, 215, 0)  // Amarillo
            };
            color = colors[random.nextInt(colors.length)];
        }
        
        void update() {
            x += speedX;
            y += speedY;
            size -= 0.2f;
            alpha -= 5;
        }
        
        void draw(Canvas canvas, Paint paint) {
            paint.setColor(color);
            paint.setAlpha(alpha);
            
            // Dibujar una forma irregular para la llama
            Path path = new Path();
            path.moveTo(x, y);
            path.lineTo(x - size/2, y + size);
            path.lineTo(x, y + size*0.8f);
            path.lineTo(x + size/2, y + size);
            path.close();
            
            canvas.drawPath(path, paint);
            
            // Dibujar un círculo en la parte superior para suavizar
            canvas.drawCircle(x, y, size/3, paint);
        }
    }
}
