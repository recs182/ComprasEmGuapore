package br.com.macropublicidade.comprasemguapore;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.com.macropublicidade.comprasemguapore.data.DatabaseHelper;
import br.com.macropublicidade.comprasemguapore.data.WebClient;

public class LoadActivity extends AppCompatActivity {

    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar) findViewById(R.id.LoadActivity_ProgressBar);

        Intent intent = new Intent(LoadActivity.this, SectorActivity.class);

        DatabaseHelper db_helper = new DatabaseHelper(LoadActivity.this);
        Boolean last_update = db_helper.checkLastUpdate();

        Log.d("LAST_UPDATE", last_update.toString());
        if( last_update ) {
            new DataHandler().execute();
        }else{
            startActivity(intent);
            finish();
        }

    }

    public class DataHandler extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object[] params) {

            WebClient client = new WebClient();
            String result = client.get();

            DatabaseHelper db_help = new DatabaseHelper(LoadActivity.this);
            db_help.syncDB(result);

            return true;
        }

        @Override
        protected void onPreExecute(){
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
        }

        @Override
        protected void onPostExecute(Boolean isSync){
            progress.setVisibility(View.GONE);
            Intent intentSector = new Intent(LoadActivity.this, SectorActivity.class);

            if( isSync ){
                startActivity(intentSector);
            }else{
                Toast.makeText(LoadActivity.this, R.string.DataHandler_Toast_message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);
        }

    }

}
