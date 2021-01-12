package amsi.dei.estg.ipleiria.am.models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amsi.dei.estg.ipleiria.am.listeners.AvariasListener;
import amsi.dei.estg.ipleiria.am.utils.AvariaJsonParser;
import amsi.dei.estg.ipleiria.am.utils.DispositivoJsonParser;
import amsi.dei.estg.ipleiria.am.utils.EstatisticaJsonParser;

public class SingletonGestorAvarias implements AvariasListener {

    private ArrayList<Avaria> avarias;
    private ArrayList<Dispositivo> dispositivos;
    private Estatistica estatistica;
    private static SingletonGestorAvarias instance = null;
    private AvariaDBHelper avariaDBHelper = null;
    private static RequestQueue volleyQueue = null;

    private AvariasListener avariasListener;
    private static final int EDITAR_DB = 2;
    private static final int ADICIONAR_DB = 1;
    private static final int REMOVER_DB = 3;

    private static final String mUrlAPIAvarias = "http://192.168.0.15:8080/avarias";
    private static final String mUrlAPIAvariasOrd = "http://192.168.0.15:8080/avarias/ordered/";
    private static final String mUrlAPIDispositivos = "http://192.168.0.15:8080/dispositivos";
    private static final String mUrlAPIEstatistica = "http://192.168.0.15:8080/relatorios/estatistica";

    public static synchronized  SingletonGestorAvarias getInstance(Context context){
        if(instance == null){
            instance = new SingletonGestorAvarias(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonGestorAvarias(Context context){
        avariaDBHelper = new AvariaDBHelper(context);
    }

    public ArrayList<Avaria> getAvariasDB() {
        avarias = avariaDBHelper.getAllAvariasDB();
        return avarias;
    }

    public Avaria getAvaria(int idAvaria) {
        if(avarias.size() > 0){
            for (Avaria avaria:avarias) {
                if(avaria.getIdAvaria() == idAvaria){
                    return avaria;
                }
            }
        }

        return null;
    }

    public void adicionarAvariaDB(Avaria avaria){ avariaDBHelper.adicionarAvariaDB(avaria); }

    public void adicionarAvariasDB(ArrayList<Avaria> livros){
        avariaDBHelper.removerAllAvariasDB();

        for(Avaria a : avarias){
            adicionarAvariaDB(a);
        }
    }

    public ArrayList<Dispositivo> getDispositivosDB() {
        dispositivos = avariaDBHelper.getAllDispositivosDB();
        return dispositivos;
    }

    public Dispositivo getDispositivo(int idDispositivo) {
        if(dispositivos.size() > 0){
            for (Dispositivo dispositivo:dispositivos) {
                if(dispositivo.getIdDispositivo() == idDispositivo){
                    return dispositivo;
                }
            }
        }

        return null;
    }

    public void adicionarDispositivoDB(Dispositivo dispositivo){ avariaDBHelper.adicionarDispositivoDB(dispositivo); }

    public void adicionarDispositivosDB(ArrayList<Dispositivo> dispositivos){
        avariaDBHelper.removerAllDispositivosDB();

        for(Dispositivo d : dispositivos){
            adicionarDispositivoDB(d);
        }
    }

    public void editarAvariaDB(Avaria avaria){
        if(!avarias.contains(avaria)){
            return;
        }
        Avaria a = getAvaria(avaria.getIdAvaria());
        a.setDescricao(avaria.getDescricao());
        a.setEstado(avaria.getEstado());
        a.setTipo(avaria.getTipo());
        a.setGravidade(avaria.getGravidade());
        a.setIdDispositivo(avaria.getIdDispositivo());

        if(avariaDBHelper.editarAvariaDB(a)){
            System.out.println("--> Avaria atualizada DB");
        }
    }

    public void removerAvariaDB(int id){
        Avaria a = getAvaria(id);

        if(a != null){
            if(avariaDBHelper.removerAvariaDB(a.getIdAvaria())){
                avarias.remove(a);
                System.out.println("--> Avaria removida DB");
            }
        }
    }



    //############################ API #################################

    public void adicionarAvariaAPI(final Avaria avaria, final Context context){
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPIAvarias, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onUpdateListaAvarias(avaria,ADICIONAR_DB);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("CARALHO->" + error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("idUtilizador", ""+1);
                params.put("idAvaria", ""+avaria.getIdAvaria());
                params.put("data", avaria.getDate());
                System.out.println("OLAS->" + avaria.getDate());
                params.put("idDispositivo", ""+avaria.getIdDispositivo());
                params.put("descricao", avaria.getDescricao());
                params.put("estado", ""+avaria.getEstado());
                params.put("gravidade", ""+avaria.getGravidade());
                params.put("tipo", ""+avaria.getTipo());

                return params;
            }
        };

        volleyQueue.add(request);
    }


    public void getAllAvariasAPI(final Context context){
        if(!AvariaJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
            if (avariasListener != null){
                avariasListener.onRefreshListaAvarias(avariaDBHelper.getAllAvariasDB());
            }
        }else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIAvariasOrd, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    avarias = AvariaJsonParser.parserJsonAvarias(response);

                    adicionarAvariasDB(avarias);

                    if(avariasListener != null){
                        avariasListener.onRefreshListaAvarias(avarias);
                    }

                    System.out.println("--> AVARIAS: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(request);
        }
    }

    public void getAllDispositivosAPI(final Context context){
        if(!DispositivoJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
        }else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIDispositivos, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    dispositivos = DispositivoJsonParser.parserJsonDispositivos(response);

                    adicionarDispositivosDB(dispositivos);

                    if(avariasListener != null){
                        avariasListener.onRefreshListaAvarias(avarias);
                    }

                    System.out.println("--> Dispositivos: " + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(request);
        }
    }

    public void removerAvariaAPI(final  Avaria  avaria,  final Context context){
        StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPIAvarias + '/' + avaria.getIdAvaria(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onUpdateListaAvarias(avaria, REMOVER_DB);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        volleyQueue.add(request);
    }



    public void editarAvariaAPI(final  Avaria  avaria,  final Context context){
        StringRequest request = new StringRequest(Request.Method.PUT, mUrlAPIAvarias + '/' + avaria.getIdAvaria(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onUpdateListaAvarias(AvariaJsonParser.parserJsonAvaria(response), EDITAR_DB);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("OIIII-> " + avaria.getIdAvaria());
                //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("idUtilizador", ""+1);
                params.put("idDispositivo", ""+avaria.getIdDispositivo());
                params.put("descricao", avaria.getDescricao());
                params.put("estado", ""+avaria.getEstado());
                params.put("gravidade", ""+avaria.getGravidade());
                params.put("tipo", ""+avaria.getTipo());

                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void setEstatisticaAPI(final Context context){
        if(!EstatisticaJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
        }else{
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrlAPIEstatistica + null, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    estatistica = EstatisticaJsonParser.parserJsonEstatisitica(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            });
            volleyQueue.add(request);
        }
    }

    public Estatistica getEstatistica(){
            return estatistica;
    }

    public void setAvariasListener(AvariasListener avariasListener){
        this.avariasListener = avariasListener;
    }

    @Override
    public void onRefreshListaAvarias(ArrayList<Avaria> listaAvaria) {

    }

    @Override
    public void onUpdateListaAvarias(Avaria avaria, int operacao) {
        switch (operacao){
            case ADICIONAR_DB:
                adicionarAvariaDB(avaria);
                break;
            case EDITAR_DB:
                editarAvariaDB(avaria);
                break;
            case REMOVER_DB:
                removerAvariaDB(avaria.getIdAvaria());
        }
    }
}
