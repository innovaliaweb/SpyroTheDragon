package dam.pmdm.spyrothedragon.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {

    private List<Character> list;

    public CharactersAdapter(List<Character> charactersList) {
        this.list = charactersList;
    }

    @Override
    public CharactersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CharactersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharactersViewHolder holder, int position) {
        Character character = list.get(position);
        holder.nameTextView.setText(character.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(character.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);
        
        // Easter Egg para Spyro (primera posición)
        if (position == 0 && "Spyro".equals(character.getName())) {
            holder.itemView.setOnLongClickListener(v -> {
                // Mostrar la llama sobre la imagen
                MostrarLlamaEgg(holder);
                return true;
            });
        }
    }

    private void MostrarLlamaEgg(CharactersViewHolder holder) {
        // Creamos una nueva ImageView para la llama
        ImageView vistaLlamaImagen = new ImageView(holder.itemView.getContext());
        vistaLlamaImagen.setImageResource(R.drawable.flame);
        
        // Configuramos el tamaño - mantenemos el 80% de la imagen de Spyro
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        
        // Hacemos la llama más grande (2/3 del tamaño de la imagen de Spyro en lugar de 1/2)
        params.width = (int)(holder.imageImageView.getWidth() * 1.05); // 120% del ancho de la imagen de Spyro
        params.height = (int)(holder.imageImageView.getHeight() * 1.05); // 120% del alto de la imagen de Spyro
        
        // Ajustamos la posición para compensar el aumento de tamaño
        // Movemos la llama más a la izquierda (a 45% en lugar de 60%)
        // y un poco más arriba (a 20% en lugar de 25%)
        params.leftMargin = (int)(holder.imageImageView.getLeft() + holder.imageImageView.getWidth() * 0.45);
        params.topMargin = (int)(holder.imageImageView.getTop() + holder.imageImageView.getHeight() * 0.05);
        
        vistaLlamaImagen.setLayoutParams(params);
        
        // Añadimos la imagen al layout
        ViewGroup container = (ViewGroup) holder.itemView;
        container.addView(vistaLlamaImagen);
        
        // Eliminamos la llama después de 3 segundos
        new Handler().postDelayed(() -> {
            container.removeView(vistaLlamaImagen);
        }, 3000);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharactersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CharactersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
