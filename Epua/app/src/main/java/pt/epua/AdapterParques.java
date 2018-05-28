package pt.epua;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterParques extends RecyclerView.Adapter<AdapterParques.MyViewHolder>{

    private final List<Parque> parqueList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView id;
        final TextView nome;
        final TextView livre;
        final TextView distancia;
        final ImageView status;

        MyViewHolder(View vw) {
            super(vw);

            id = vw.findViewById(R.id.tv_id);
            nome = vw.findViewById(R.id.tv_nome);
            livre = vw.findViewById(R.id.tv_livre);
            distancia = vw.findViewById(R.id.tv_distancia);
            status = vw.findViewById(R.id.tv_status);
        }
    }

    public AdapterParques(List<Parque> parqueList) {
        this.parqueList = parqueList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parques_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Parque parque = parqueList.get(position);

        int livre = parque.livre;
        int capacidade = parque.capacidade;
        double ocupado = capacidade-livre;
        double ratio = ( ocupado/capacidade) * 100;

        ImageView status = holder.status;
        // 100% red 75% yellow 50% green
        if (ratio==100) status.setImageResource(R.drawable.cancel);
        if (ratio<100 && ratio>=75) status.setImageResource(R.drawable.attention);
        if (ratio<75) status.setImageResource(R.drawable.check);

        //Populate views
        holder.id.setText(parque.getId());
        holder.nome.setText(parque.getNome());
        holder.livre.setText(String.valueOf(parque.getLivre()));
        holder.distancia.setText(String.valueOf(parque.getDistancia()));
    }

    @Override
    public int getItemCount() {
        return parqueList.size();
    }
}
