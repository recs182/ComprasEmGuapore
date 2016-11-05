package br.com.macropublicidade.comprasemguapore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.macropublicidade.comprasemguapore.R;
import br.com.macropublicidade.comprasemguapore.models.Company;

/**
 * Created by renan on 11/5/2016.
 */

public class ListCompaniesAdapter extends BaseAdapter {

    private final List<Company> companies;
    private final Context context;

    private String[] url_logo = new String[]{"http://www.comprasemguapore.com.br/images/companies/logos/", "_logo.jpg"};

    public ListCompaniesAdapter(Context context, List<Company> companies){
        this.companies = companies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Object getItem(int position) {
        return companies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return companies.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Company company = companies.get(position);
        View view = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);

        if(view == null){
            view = inflater.inflate(R.layout.adapter_list_companies, parent, false);
        }

        ImageView logo = (ImageView) view.findViewById(R.id.ListCompaniesAdapter_ImageView);
        Picasso.with(context).load(url_logo[0] + company.getId().toString() + url_logo[1]).fit().placeholder(R.drawable.default_logo).into(logo);

        TextView textView = (TextView) view.findViewById(R.id.ListCompaniesAdapter_TextView);
        textView.setText( company.getCompany() );

        return view;
    }
}
