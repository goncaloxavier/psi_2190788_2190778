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
            int numAvarias = estatistica.getInt("avariasTotal");
            int numAvariasNR = estatistica.getInt("avariasNR");
            int numAvariasR = estatistica.getInt("avariasR");
            int numDispositivosNF = estatistica.getInt("dispositivosNF");
            int numDispositivosF = estatistica.getInt("dispositivosF");

            auxEstatistica = new Estatistica(numAvarias, numAvariasR, numAvariasNR, numDispositivosF, numDispositivosNF);
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
