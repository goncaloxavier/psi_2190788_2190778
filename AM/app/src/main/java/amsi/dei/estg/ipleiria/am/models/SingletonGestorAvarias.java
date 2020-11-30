package amsi.dei.estg.ipleiria.am.models;

import android.content.Context;

import java.util.ArrayList;

public class SingletonGestorAvarias {

    private ArrayList<Avaria> avarias;
    private static SingletonGestorAvarias instance = null;
    private AvariaDBHelper avariaDBHelper = null;

    public static synchronized  SingletonGestorAvarias getInstance(Context context){
        if(instance == null){
            instance = new SingletonGestorAvarias(context);
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

        if(auxAvaria != null){
            avarias.add(avaria);
            System.out.println("--> Avaria ADD DB");
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
}
