package br.com.macropublicidade.comprasemguapore.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.macropublicidade.comprasemguapore.R;
import br.com.macropublicidade.comprasemguapore.models.Sector;

/**
 * Created by renan on 8/30/2016.
 */
public class SectorAdapter extends BaseAdapter {

    private final List<Sector> sectors;
    private final Context context;

    public SectorAdapter(Context context, List<Sector> sectors){
        this.context = context;
        this.sectors = sectors;
    }

    @Override
    public int getCount() {
        return sectors.size();
    }

    @Override
    public Object getItem(int position) {
        return sectors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sectors.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sector sector = sectors.get(position);
        View view = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);

        if( view == null ) {
            view = inflater.inflate(R.layout.adapter_sector, parent, false);
        }

        String imageName = sector.getName().substring(0,3).toLowerCase();
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        TextView textView = (TextView) view.findViewById(R.id.SectorAdapter_TextView);
        textView.setText( sector.getName() );

        ImageView imageView = (ImageView) view.findViewById(R.id.SectorAdapter_ImageView);
        Picasso.with(context).load(resourceId).fit().into(imageView);

        return view;
    }

}
