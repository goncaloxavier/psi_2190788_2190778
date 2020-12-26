package amsi.dei.estg.ipleiria.am.models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.listeners.AvariasListener;
import amsi.dei.estg.ipleiria.am.utils.AvariaJsonParser;
import amsi.dei.estg.ipleiria.am.utils.EstatisticaJsonParser;

public class SingletonGestorAvarias implements AvariasListener {

    private ArrayList<Avaria> avarias;
    private Estatistica estatistica;
    private static SingletonGestorAvarias instance = null;
    private AvariaDBHelper avariaDBHelper = null;
    private static RequestQueue volleyQueue = null;

    private AvariasListener avariasListener;

    private static final String mUrlAPIAvarias = "http://10.200.18.73:8888/avaria";
    private static final String mUrlAPIEstatistica = "http://10.200.18.73:8888/estatistica/estatistica/";

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
        for (Avaria avaria:avarias) {
            if(avaria.getIdAvaria() == idAvaria){
                return avaria;
            }
        }
        return null;
    }

    public void adicionarAvariaDB(Avaria avaria){
        Avaria auxAvaria = avariaDBHelper.adicionarAvariaDB(avaria);
    }

    public void adicionarAvariasDB(ArrayList<Avaria> livros){
        avariaDBHelper.removerAllAvariasDB();

        for(Avaria a : avarias){
            adicionarAvariaDB(a);
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
        a.setDate(avaria.getDate());
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

    /*public void adicionarLivroAPI(final Livro livro, final Context context){
        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPILivros, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("token", "AMSI-TOKEN");
                params.put("titulo", livro.getTitulo());
                params.put("serie", livro.getSerie());
                params.put("autor", livro.getAutor());
                params.put("ano", ""+livro.getAno());
                params.put("capa", livro.getCapa() == null ? NOIMAGE : livro.getCapa());

                return params;
            }
        };

        volleyQueue.add(request);
    }*/

    public void getAllAvariasAPI(final Context context){
        if(!AvariaJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Nao tem ligacao a rede!!", Toast.LENGTH_SHORT).show();
            if (avariasListener != null){
                avariasListener.onRefreshListaAvarias(avariaDBHelper.getAllAvariasDB());
            }
        }else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPIAvarias, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    avarias = AvariaJsonParser.parserJsonAvaria(response);

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

    /*
    public void removerLivroAPI(final  Livro  livro,  final Context context){
        StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPILivros + '/' + livro.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        volleyQueue.add(request);
    }*/


    /*
    public void editarLivroAPI(final  Livro  livro,  final Context context){
        StringRequest request = new StringRequest(Request.Method.PUT, mUrlAPILivros + '/' + livro.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("token", "AMSI-TOKEN");
                params.put("titulo", livro.getTitulo());
                params.put("serie", livro.getSerie());
                params.put("autor", livro.getAutor());
                params.put("ano", ""+livro.getAno());
                params.put("capa", livro.getCapa() == null ? NOIMAGE : livro.getCapa());

                return params;
            }
        };

        volleyQueue.add(request);
    }*/

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

    }
}
