package br.com.macropublicidade.comprasemguapore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import br.com.macropublicidade.comprasemguapore.adapters.SectorAdapter;
import br.com.macropublicidade.comprasemguapore.data.DatabaseHelper;
import br.com.macropublicidade.comprasemguapore.models.Sector;

public class SectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);

        DatabaseHelper db_helper = new DatabaseHelper(SectorActivity.this);
        final List<Sector> list_sectors = new ArrayList<>(db_helper.getSectors());

        GridView grid_view = (GridView) findViewById(R.id.SectorActivity_GridView);
        grid_view.setAdapter( new SectorAdapter(SectorActivity.this, list_sectors) );

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Sector sector = list_sectors.get(position);

            Intent intentListGroups = new Intent(SectorActivity.this, ListGroupsActivity.class);
            intentListGroups.putExtra("sector", sector);
            startActivity(intentListGroups);
            }
        });

    }
}
