package amsi.dei.estg.ipleiria.am.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import amsi.dei.estg.ipleiria.am.models.Estatistica;

public class EstatisticaJsonParser {

    public static Estatistica parserJsonEstatisitica(JSONObject response){
        Estatistica auxEstatistica = new Estatistica();
        try{
            JSONObject estatistica = response;
            int numAvarias = estatistica.getInt("totalAvarias");
            int numAvariasNR = estatistica.getInt("totalAvariasNR");
            int numAvariasR = estatistica.getInt("totalAvariasR");
            int numDispositivos = estatistica.getInt("totalDispositivoT");
            int numDispositivosNF = estatistica.getInt("totalDispositivoNF");
            int numDispositivosF = estatistica.getInt("totalDispositivoF");

            auxEstatistica = new Estatistica(numAvarias, numDispositivos, numAvariasR, numAvariasNR, numDispositivosF, numDispositivosNF);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return auxEstatistica;
    }

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
