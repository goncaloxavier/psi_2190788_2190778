package amsi.dei.estg.ipleiria.am.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.Utilizador;

public class UtilizadorJsonParser {

    public static ArrayList<Utilizador> parserJsonUtilizadores(JSONArray response){
        ArrayList<Utilizador> listaUtilizadores = new ArrayList<>();

        try{
            for (int i = 0; i < response.length(); i++){
                JSONObject utilizador = (JSONObject) response.get(i);

                int idUtilizador = utilizador.getInt("idUtilizador");
                int tipo = utilizador.getInt("tipo");
                int estado = utilizador.getInt("estado");
                String nomeUtilizador = utilizador.getString("nomeUtilizador");
                String palavraPasse = utilizador.getString("palavraPasse");
                String email = utilizador.getString("email");

                Utilizador auxUtilizador = new Utilizador(nomeUtilizador, palavraPasse, email, tipo, estado, idUtilizador);
                listaUtilizadores.add(auxUtilizador);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return listaUtilizadores;
    }

    public static Utilizador parserJsonUtilizador(String response){
        Utilizador auxUtilizador = null;

        try{
            if(response != null){
                JSONObject utilizador = new JSONObject(response);
                int idUtilizador = utilizador.getInt("idUtilizador");
                int tipo = utilizador.getInt("tipo");
                int estado = utilizador.getInt("estado");
                String nomeUtilizador = utilizador.getString("nomeUtilizador");
                String palavraPasse = utilizador.getString("palavraPasse");
                String email = utilizador.getString("email");

                auxUtilizador = new Utilizador(nomeUtilizador, palavraPasse, email, tipo, estado, idUtilizador);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return auxUtilizador;
    }


    public static boolean isConnectionInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
