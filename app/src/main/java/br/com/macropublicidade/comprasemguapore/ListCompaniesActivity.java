package br.com.macropublicidade.comprasemguapore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.macropublicidade.comprasemguapore.data.DatabaseHelper;
import br.com.macropublicidade.comprasemguapore.models.Company;
import br.com.macropublicidade.comprasemguapore.models.Group;
import br.com.macropublicidade.comprasemguapore.models.Sector;

public class ListCompaniesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_companies);

        final Bundle extras = getIntent().getExtras();
        Group group = (Group) extras.getSerializable("group");
        Sector sector = (Sector) extras.getSerializable("sector");

        if( group.getName().equals("Único") ){
            setTitle( sector.getName() );
        }else{
            setTitle( group.getName() );
        }

        DatabaseHelper db_helper = new DatabaseHelper(this);

        final List<Company> list_companies = db_helper.getCompanies( group.getId().toString() );

        ListView listView = (ListView) findViewById(R.id.ListCompanies_ListView);
        ArrayAdapter<Company> adapater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_companies);
        listView.setAdapter(adapater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Company company = list_companies.get(position);

                extras.putSerializable("company", company);
                Intent intentCompany = new Intent(ListCompaniesActivity.this, CompanyActivity.class);
                intentCompany.putExtras(extras);
                startActivity(intentCompany);

            }
        });
    }
}
