package amsi.dei.estg.ipleiria.am.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.models.Avaria;

public class AvariaJsonParser {

    public static ArrayList<Avaria> parserJsonAvarias(JSONArray response){
        ArrayList<Avaria> listaAvaria = new ArrayList<>();

        try{
            for (int i = 0; i < response.length(); i++){
                JSONObject avaria = (JSONObject) response.get(i);
                int idAvaria = avaria.getInt("idAvaria");
                int estado = avaria.getInt("estado");
                int tipo = avaria.getInt("tipo");
                int gravidade = avaria.getInt("gravidade");
                int idDispositivo = avaria.getInt("idDispositivo");
                int idUtilizador = avaria.getInt("idUtilizador");
                String descricao = avaria.getString("descricao");
                String data = avaria.getString("data");

                Avaria auxAvaria = new Avaria(idAvaria, estado, gravidade, tipo, idDispositivo, data, descricao,idUtilizador);
                listaAvaria.add(auxAvaria);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return listaAvaria;
    }

    public static Avaria parserJsonAvaria(String response){
        Avaria auxAvaria = null;

        try{
            JSONObject avaria = new JSONObject(response);
            int idAvaria = avaria.getInt("idAvaria");
            int estado = avaria.getInt("estado");
            int tipo = avaria.getInt("tipo");
            int gravidade = avaria.getInt("gravidade");
            int idDispositivo = avaria.getInt("idDispositivo");
            int idUtilizador = avaria.getInt("idUtilizador");
            String descricao = avaria.getString("descricao");
            String data = avaria.getString("data");

            auxAvaria = new Avaria(idAvaria, estado, gravidade, tipo, idDispositivo, data, descricao,idUtilizador);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return auxAvaria;
    }

    public static String parserJsonLogin(String response){
        String token = null;

        try{
            JSONObject login = new JSONObject(response);
            if(login.getBoolean("success")){
                token = login.getString("token");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return token;
    }

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
