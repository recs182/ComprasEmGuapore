package br.com.macropublicidade.comprasemguapore.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import br.com.macropublicidade.comprasemguapore.models.Company;
import br.com.macropublicidade.comprasemguapore.models.Group;
import br.com.macropublicidade.comprasemguapore.models.Sector;
import br.com.macropublicidade.comprasemguapore.models.Subgroup;

/**
 * Created by renan on 8/28/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "ComprasEmGuapore.db";
    private static final int DB_VERSION = 1;

    private ArrayList<String> sql = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();
    private String current_date = calendar.get( Calendar.DAY_OF_MONTH ) + "/" + ( "0" + calendar.get( Calendar.MONTH ) ).substring(0, 2) + "/" + calendar.get( Calendar.YEAR );

    private String atualizar = "0";
    private String last_login = "0";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        sql.add("CREATE TABLE IF NOT EXISTS empresa (id integer primary key, empresa text, endereco text, descricao text, telefone text, email text, coordenadas text, webpage text, facebook text, instagram text, twitter text, horarios text, verificado text, atualizar text, ativo text, whats text)");
        sql.add("CREATE TABLE IF NOT EXISTS empresa_grupo_controle (id integer primary key, id_empresa text, id_setor text, id_grupo text, id_subgrupo text, ordem text, atualizar text, ativo text)");
        sql.add("CREATE TABLE IF NOT EXISTS setor (id integer primary key, nome text, atualizar text, ativo text)");
        sql.add("CREATE TABLE IF NOT EXISTS grupo (id integer primary key, id_setor text, nome text, atualizar text, ativo text)");
        sql.add("CREATE TABLE IF NOT EXISTS subgrupo (id integer primary key, id_grupo text, nome text, atualizar text, ativo text)");
        sql.add("CREATE TABLE IF NOT EXISTS horarios (id integer primary key, id_empresa text, dia_semana text, horario1 text, horario2 text, horario3 text, horario4 text, atualizar text, ativo text)");
        sql.add("CREATE TABLE IF NOT EXISTS atualizar (id integer primary key, atualizar text, atualizar_img text, last_login text)");
        sql.add("CREATE TABLE IF NOT EXISTS galeria (id integer primary key, id_empresa text, imagem text)");

        for( String row : sql ){
            db.execSQL(row);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //in case of changes in database, add code bellow and increment DB_VERSION
        switch (oldVersion){
            case 1:
                break;
        }
    }

    /**
     * method: checkUpdateDB
     * check if table "atualizar" has something, if isn't insert some values and proceed with the sync
     */
    public Boolean checkLastUpdate(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT atualizar, last_login from atualizar where id = ?", new String[] {"1"});
        while( c.moveToNext() ){
            atualizar = c.getString( c.getColumnIndex("atualizar") );
            last_login = c.getString( c.getColumnIndex("last_login") );
            c.close();
            Log.d("LAST_LOGIN_INNER", last_login);
        }
        Log.d("LAST_LOGIN", last_login);
        Log.d("LAST_CURRENT", current_date);
        return !last_login.equals( current_date );
    }

    /**
     * method: syncDB
     * synchronise database with returned result of WebClient
     */
    public void syncDB(String result){

        try{
            JSONObject json = new JSONObject( result );
            Iterator<?> tables = json.keys();

            //object of "tables"
            while( tables.hasNext() ){
                String table = tables.next().toString();
                String fields;

                LinkedHashSet<String> fields_list = new LinkedHashSet<>();
                ArrayList<String> values = new ArrayList<>();

                //Array of Objects
                JSONArray array_obj = json.getJSONArray(table);

                for (int i = 0; i < array_obj.length(); i++) {
                    ArrayList<String> value = new ArrayList<>();
                    JSONObject obj_values = array_obj.getJSONObject(i);

                    Iterator<?> iterator = obj_values.keys();

                    //object of values
                    while (iterator.hasNext()) {
                        String column = iterator.next().toString();
                        fields_list.add(column);
                        value.add(obj_values.get(column).toString());
                    }
                    //values concat
                    values.add( ",(\"" + TextUtils.join("\",\"", value) + "\")" );
                }

                //fields concat
                fields = TextUtils.join(",", fields_list);

                int steps = 499;
                int end = steps;
                for( int start = 0; start < values.size(); start += steps ){
                    if( end > values.size() ){
                        end = values.size();
                    }
                    List tmp_list = values.subList(start, end);
                    String query = "INSERT OR REPLACE INTO " + table + "(" + fields + ") VALUES " + TextUtils.join("", tmp_list).substring(1) + "";
                    this.insert(query);

                    end += steps;
                }
            }

           int dateUnix = (int) (System.currentTimeMillis() / 1000L);

            ContentValues values = new ContentValues();
            values.put("atualizar", dateUnix);
            values.put("last_login", current_date);
            this.updateById("atualizar", values, "1");

        }catch(JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insert(String values_to_insert){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(values_to_insert);
        db.close();
    }

    public void updateById(String table, ContentValues values, String id){
        SQLiteDatabase db = getWritableDatabase();
        Log.d("LAST_CONTENT_VALUES", values.toString());
        db.update(table, values, "id = ?", new String[]{ id });
        db.close();
    }

    public List<Sector> getSectors(){
        String sql = "SELECT id, nome FROM setor";
        SQLiteDatabase db = getReadableDatabase();
        List<Sector> sectors = new ArrayList<>();

        Cursor c = db.rawQuery(sql, null);
        while(c.moveToNext()){
            Sector sector = new Sector();
            sector.setName( c.getString(c.getColumnIndex("nome")) );
            sector.setId( c.getLong(c.getColumnIndex("id")) );
            sectors.add(sector);
        }
        c.close();
        db.close();
        return sectors;
    }

    public List<Group> getGroups( String id_sector ){
        String sql = "SELECT id, nome, id_setor FROM grupo where id_setor = ?";
        SQLiteDatabase db = getReadableDatabase();
        List<Group> groups = new ArrayList<>();

        Cursor c = db.rawQuery(sql, new String[]{ id_sector });
        while(c.moveToNext()){
            Group group = new Group();
            group.setId( c.getLong(c.getColumnIndex("id")) );
            group.setName( c.getString(c.getColumnIndex("nome")) );
            group.setId_sector( c.getString(c.getColumnIndex("id_setor")) );

            groups.add(group);
        }
        c.close();
        db.close();
        return groups;
    }

    public List<Subgroup> getSubgroups( String id_group ){
        String sql = "SELECT * from subgrupo where id_grupo = ?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql, new String[]{ id_group });
        List<Subgroup> subgroups = new ArrayList<>();
        while( c.moveToNext() ){
            Subgroup subgroup = new Subgroup();

            subgroup.setId( c.getLong(c.getColumnIndex("id")) );
            subgroup.setName( c.getString(c.getColumnIndex("nome")) );
            subgroup.setId_group( c.getString(c.getColumnIndex("id_grupo")) );

            subgroups.add(subgroup);
        }
        c.close();
        db.close();
        return subgroups;
    }

    public List<Company> getCompanies( String id_grupo ){
        String sql = "SELECT e.* from empresa e left join empresa_grupo_controle egc on (egc.id_empresa = e.id) where egc.id_grupo = ? order by e.empresa";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql, new String[]{ id_grupo });
        List<Company> companies = new ArrayList<>();
        while( c.moveToNext() ){
            Company company = new Company();

            company.setId( c.getLong(c.getColumnIndex("id")) );
            company.setCompany( c.getString(c.getColumnIndex("empresa")) );
            company.setAddress( c.getString(c.getColumnIndex("endereco")) );
            company.setCoordinates( c.getString(c.getColumnIndex("coordenadas")) );
            company.setDescription( c.getString(c.getColumnIndex("descricao")) );
            company.setEmail( c.getString(c.getColumnIndex("email")) );
            company.setFacebook( c.getString(c.getColumnIndex("facebook")) );
            company.setHours( c.getString(c.getColumnIndex("horarios")) );
            company.setInstagram( c.getString(c.getColumnIndex("instagram")) );
            company.setPhone( c.getString(c.getColumnIndex("telefone")) );
            company.setTwitter( c.getString(c.getColumnIndex("twitter")) );
            company.setWhatsapp( c.getString(c.getColumnIndex("whats")) );

            companies.add(company);
        }
        c.close();
        db.close();
        return companies;
    }

}
