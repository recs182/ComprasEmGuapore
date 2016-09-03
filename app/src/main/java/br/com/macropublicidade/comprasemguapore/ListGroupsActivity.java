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
import br.com.macropublicidade.comprasemguapore.models.Group;
import br.com.macropublicidade.comprasemguapore.models.Sector;

public class ListGroupsActivity extends AppCompatActivity {

    private Bundle extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_groups);

        final Intent intentCompanies = new Intent(ListGroupsActivity.this, ListCompaniesActivity.class);

        Intent intentSelf = getIntent();
        final Sector sector = (Sector) intentSelf.getSerializableExtra("sector");
        extras.putSerializable("sector", sector);

        setTitle( sector.getName() );

        DatabaseHelper db_helper = new DatabaseHelper(ListGroupsActivity.this);
        final List<Group> list_groups = db_helper.getGroups( sector.getId().toString() );


        if( list_groups.size() == 1 ){

            Group group = list_groups.get(0);

            extras.putSerializable("group", group);
            intentCompanies.putExtras(extras);
            startActivity(intentCompanies);
            finish();
        }

        ListView listView = (ListView) findViewById(R.id.ListGroupsActivity_ListView);
        ArrayAdapter<?> adapter = new ArrayAdapter<>(ListGroupsActivity.this, android.R.layout.simple_list_item_1, list_groups);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = list_groups.get(position);

                extras.putSerializable("group", group);
                intentCompanies.putExtras(extras);
                startActivity(intentCompanies);
            }
        });

    }
}
