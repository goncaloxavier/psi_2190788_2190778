package amsi.dei.estg.ipleiria.am.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import amsi.dei.estg.ipleiria.am.listeners.VolleyListener;
import amsi.dei.estg.ipleiria.am.utils.AvariaJsonParser;
import amsi.dei.estg.ipleiria.am.utils.DispositivoJsonParser;
import amsi.dei.estg.ipleiria.am.utils.EstatisticaJsonParser;
import amsi.dei.estg.ipleiria.am.utils.UtilizadorJsonParser;

public class SingletonGestorAvarias implements AvariasListener, VolleyListener {

    private ArrayList<Avaria> avarias;
    private ArrayList<Dispositivo> dispositivos;
    private Estatistica estatistica;
    private Utilizador utilizador;
    private ArrayList<Utilizador> utilizadores;
    private static SingletonGestorAvarias instance = null;
    private AvariaDBHelper avariaDBHelper = null;
    private static RequestQueue volleyQueue = null;

    private AvariasListener avariasListener;
    private static final int EDITAR_DB = 2;
    private static final int ADICIONAR_DB = 1;
    private static final int REMOVER_DB = 3;

    private VolleyListener volleyListener;

    private static final String mUrlAPIAvarias = "http://192.168.1.125:8080/avarias";
    private static final String mUrlAPIAvariasOrd = "http://192.168.1.125:8080/avarias/ordered/";
    private static final String mUrlAPIDispositivos = "http://192.168.1.125:8080/dispositivos";
    private static final String mUrlAPIEstatistica = "http://192.168.1.125:8080/relatorios/estatistica/anual";
    private static final String mUrlAPILogin = "http://192.168.1.125:8080/utilizadores/auth/";
    private static final String mUrlAvariasByUser = "http://192.168.1.125:8080/avarias/byuser/";
    private static final String getmUrlAvariasbyRef = "http://192.168.1.125:8080/avarias/byref/";
    private static final String getmUrlAPIUtilizadores = "http://192.168.1.125:8080/utilizadores/";


    public static synchronized SingletonGestorAvarias getInstance(Context context){
        if(instance == null){
            instance = new SingletonGestorAvarias(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    public SingletonGestorAvarias(Context context){
        avariaDBHelper = new AvariaDBHelper(context);
    }

    public ArrayList<Avaria> getAvariasDB() {
        avarias = avariaDBHelper.getAllAvariasDB();
        return avarias;
    }

    public ArrayList<Avaria> getAvarias() {
        if(avarias != null){
            if(avarias.size() > 0){
                return avarias;
            }
        }

        return null;
    }

    public Avaria getAvaria(int idAvaria) {
        if(avarias != null){
            if(avarias.size() > 0){
                for (Avaria avaria:avarias) {
                    if(avaria.getIdAvaria() == idAvaria){
                        return avaria;
                    }
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
        if(dispositivos != null){
            if(dispositivos.size() > 0){
                for (Dispositivo dispositivo:dispositivos) {
                    if(dispositivo.getIdDispositivo() == idDispositivo){
                        return dispositivo;
                    }
                }
            }
        }

        return null;
    }

    public ArrayList<Utilizador> getUtilizadores() {
        if(utilizadores != null){
            if(utilizadores.size() > 0){
                return utilizadores;
            }
        }

        return null;
    }

    public Utilizador getUtilizador(int idUtilizador) {
        if(utilizadores != null){
            if(utilizadores.size() > 0){
                for (Utilizador utilizador: utilizadores) {
                    if(utilizador.getIdUtilizador() == idUtilizador){
                        return utilizador;
                    }
                }
            }
        }

        return null;
    }

    public ArrayList<Dispositivo> getDispositivos() {
        if(dispositivos.size() > 0){
            return dispositivos;
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
                onUpdateListaAvarias(avaria, avarias, ADICIONAR_DB);
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
                params.put("idUtilizador", ""+avaria.getIdUtilizador());
                params.put("data", avaria.getDate());
                params.put("idDispositivo", ""+avaria.getIdDispositivo());
                params.put("descricao", avaria.getDescricao());
                params.put("estado", ""+avaria.getEstado());
                params.put("gravidade", ""+avaria.getGravidade());
                params.put("tipo", ""+avaria.getTipo());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
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
                        (new Handler()).postDelayed(this::loadAdapter, 1000);
                    }

                    System.out.println("--> AVARIAS: " + response);
                }

                private void loadAdapter() {
                    avariasListener.onRefreshListaAvarias(avarias);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                  //// Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };



            volleyQueue.add(request);
        }
    }

    public void getAllAvariasUserAPI(final Context context){
        if(!AvariaJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
            if (avariasListener != null){
                avariasListener.onRefreshListaAvarias(avariaDBHelper.getAvariasByUserDB(utilizador.getIdUtilizador()));
            }
        }else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAvariasByUser + utilizador.getNomeUtilizador(), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(response != null){
                        if(response.length() > 0){
                            avarias = AvariaJsonParser.parserJsonAvarias(response);

                            if(avariasListener != null){
                                (new Handler()).postDelayed(this::loadAdapter, 1000);

                            }
                        }else{
                            Toast.makeText(context, "Não tem avarias associadas!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                private void loadAdapter() {
                    avariasListener.onRefreshListaAvarias(avarias);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    //// Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };



            volleyQueue.add(request);
        }
    }

    public void getAllUsersAPI(final Context context){
        if(!AvariaJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
            if (avariasListener != null){
                avariasListener.onRefreshListaAvarias(avariaDBHelper.getAllAvariasDB());
            }
        }else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getmUrlAPIUtilizadores, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    utilizadores = UtilizadorJsonParser.parserJsonUtilizadores(response);
                    System.out.println(response);
                    //adicionarUtilizadoresDB(utilizadores);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    //// Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };



            volleyQueue.add(request);
        }
    }

    public void loginAPI(final String username, final String password, final Context context){
        if(!UtilizadorJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
        }else{
            final StringRequest request = new StringRequest(Request.Method.GET, mUrlAPILogin + username + "/" + password, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response != null){
                        if(volleyListener != null){
                            utilizador = UtilizadorJsonParser.parserJsonUtilizador(response);
                            volleyListener.requestFinished(true);
                        }
                    }else{
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    volleyListener.requestFinished(false);
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
                    System.out.println("--> Dispositivos: " + response);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(request);
        }
    }

    public void getDispositivobyRef(final Context context, String query){
        if(!DispositivoJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
        }else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getmUrlAvariasbyRef + query, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    System.out.println(response);
                    if (response != null){
                        if(response.length() > 0){
                            avarias = AvariaJsonParser.parserJsonAvarias(response);
                        }else{
                            avarias = null;
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("VOLLEY ERROR");
                    if(avarias != null){
                        avarias.clear();
                    }
                  ///  Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    public void removerAvariaAPI(final  Avaria  avaria,  final Context context){
        StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPIAvarias + '/' + avaria.getIdAvaria(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onUpdateListaAvarias(avaria, avarias, REMOVER_DB);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };


        volleyQueue.add(request);
    }



    public void editarAvariaAPI(final  Avaria  avaria,  final Context context){
        StringRequest request = new StringRequest(Request.Method.PUT, mUrlAPIAvarias + '/' + avaria.getIdAvaria(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onUpdateListaAvarias(AvariaJsonParser.parserJsonAvaria(response), avarias, EDITAR_DB);
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void setEstatisticaAPI(final Context context){
        if(!EstatisticaJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
        }else{
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrlAPIEstatistica, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    estatistica = EstatisticaJsonParser.parserJsonEstatisitica(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", utilizador.getNomeUtilizador(), utilizador.getPalavraPasse());
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };

            volleyQueue.add(request);
        }
    }

    public Estatistica getEstatistica(){
            return estatistica;
    }


    public void setAvariasListener(AvariasListener avariasListener){
        this.avariasListener = avariasListener;
    }

    public void setVolleyListener(VolleyListener volleyListener){
        this.volleyListener = volleyListener;
    }

    @Override
    public void onRefreshListaAvarias(ArrayList<Avaria> listaAvaria) {

    }

    @Override
    public void onUpdateListaAvarias(Avaria avaria, ArrayList avarias,int operacao) {
        switch (operacao){
            case ADICIONAR_DB:
                adicionarAvariasDB(avarias);
                break;
            case EDITAR_DB:
                editarAvariaDB(avaria);
                break;
            case REMOVER_DB:
                removerAvariaDB(avaria.getIdAvaria());
        }
    }

    //Utilizador
    public Utilizador getUtilizador(){
        return utilizador;
    }

    @Override
    public void requestFinished(boolean exsitance) {

    }
}
