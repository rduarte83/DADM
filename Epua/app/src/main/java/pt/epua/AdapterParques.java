package pt.epua;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterParques extends BaseAdapter {

    private final List<Parque> parqueList;
    private final Activity act;

    public AdapterParques(List<Parque> parqueList, Activity act) {
        this.parqueList = parqueList;
        this.act = act;
    }

    @Override
    public int getCount() {
        return parqueList.size();
    }

    @Override
    public Object getItem(int i) {
        return parqueList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vw = act.getLayoutInflater().inflate(R.layout.parques_item, viewGroup, false);
        TextView tv_id = vw.findViewById(R.id.tv_id);
        TextView tv_nome = vw.findViewById(R.id.tv_nome);
        TextView tv_livre = vw.findViewById(R.id.tv_livre);
        ImageView status = vw.findViewById(R.id.status);

        Parque parque = parqueList.get(i);
        int livre = parque.livre;
        int capacidade = parque.capacidade;
        double ocupado = capacidade-livre;
        double ratio = ( ocupado/capacidade) * 100;
        // 100% red 75% yellow 50% green
        Log.v("capacidade",String.valueOf(capacidade));
        Log.v("livre",String.valueOf(livre));
        Log.v("ratio",String.valueOf(ratio));
        if (ratio==100) status.setImageResource(R.drawable.cancel);
        if (ratio<100 && ratio>=75) status.setImageResource(R.drawable.attention);
        if (ratio<75) status.setImageResource(R.drawable.check);




        //Views populate
        tv_id.setText(parque.getId());
        tv_nome.setText(parque.getNome());
        tv_livre.setText(String.valueOf(parque.getLivre()));

        return vw;
    }
}
