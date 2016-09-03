package br.com.macropublicidade.comprasemguapore.data;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by renan on 8/28/2016.
 */
public class WebClient {

    HttpURLConnection conn;
    URL url;
    StringBuilder result;

    Calendar calendar = Calendar.getInstance();
    private String current_date = calendar.get( Calendar.DAY_OF_MONTH ) + "/" + ( "0" + calendar.get( Calendar.MONTH ) ).substring(0, 2) + "/" + calendar.get( Calendar.YEAR );

    private String url_conn = "http://www.comprasemguapore.com.br/api/sync.php?sync=1";

    public String get(){

        try{
            url = new URL(url_conn);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.connect();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader( new InputStreamReader(in) );
            result = new StringBuilder();

            String line;
            while( (line = reader.readLine()) != null ){
                result.append(line);
            }

            conn.disconnect();

            return result.toString();

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
