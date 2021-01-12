package amsi.dei.estg.ipleiria.am.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.Dispositivo;

public class DispositivoJsonParser {

    public static ArrayList<Dispositivo> parserJsonDispositivos(JSONArray response){
        ArrayList<Dispositivo> listaDispositivo = new ArrayList<>();

        try{
            for (int i = 0; i < response.length(); i++){
                JSONObject dispositivo = (JSONObject) response.get(i);
                int idDispositivo = dispositivo.getInt("idDispositivo");
                int estado = dispositivo.getInt("estado");
                String tipo = dispositivo.getString("tipo");
                String dataCompra = dispositivo.getString("dataCompra");
                String referencia = dispositivo.getString("referencia");

                Dispositivo auxDispositivo = new Dispositivo(idDispositivo, estado, dataCompra, tipo, referencia);
                listaDispositivo.add(auxDispositivo);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return listaDispositivo;
    }

    public static Dispositivo parserJsonDispositivo(String response){
        Dispositivo auxDispositivo = null;

        try{
            JSONObject dispositivo = new JSONObject(response);
            int idDispositivo = dispositivo.getInt("idDispositivo");
            int estado = dispositivo.getInt("estado");
            String tipo = dispositivo.getString("tipo");
            String dataCompra = dispositivo.getString("dataCompra");
            String referencia = dispositivo.getString("referencia");

            auxDispositivo = new Dispositivo(idDispositivo, estado, dataCompra, tipo, referencia);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return auxDispositivo;
    }

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
