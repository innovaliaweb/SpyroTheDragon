package dam.pmdm.spyrothedragon.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Collectible;
import dam.pmdm.spyrothedragon.easterEggs.VideoEasterEggActivity;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;
    private int clickCuenta = 0;
    private long ultimoClickTiempo = 0;
    private static final int CLICKS_REQUERIDOS = 4;
    private static final long MAX_TIEMPO_CLICKS = 2000; // 2 segundos

    public CollectiblesAdapter(List<Collectible> collectibleList) {
        this.list = collectibleList;
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        // Easter Egg de la gema (posición 1 en la lista)
        if (position == 1 && "Gemas".equals(collectible.getName())) {
            holder.itemView.setOnClickListener(v -> {
                long currentTime = System.currentTimeMillis();
                
                // Si ha pasado demasiado tiempo, reiniciamos el contador
                if (currentTime - ultimoClickTiempo > MAX_TIEMPO_CLICKS) {
                    clickCuenta = 0;
                }
                
                ultimoClickTiempo = currentTime;
                clickCuenta++;
                
                // Si alcanzamos los 4 clics consecutivos, activamos el Easter Egg
                if (clickCuenta >= CLICKS_REQUERIDOS) {
                    // Reiniciamos el contador
                    clickCuenta = 0;
                    
                    // Lanzamos la actividad que muestra el video
                    Intent intent = new Intent(holder.itemView.getContext(), VideoEasterEggActivity.class);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
