package amsi.dei.estg.ipleiria.am.modelo;

import java.util.ArrayList;

public class SingletonGestorAvarias {

    private ArrayList<Avaria> avarias;
    private static SingletonGestorAvarias instance = null;


    public static synchronized  SingletonGestorAvarias getInstance(){
        if(instance == null){
            instance = new SingletonGestorAvarias();
        }
        return instance;
    }


    public void removerAvaria(int id){
        Avaria l =  getAvaria(id);
        if (l != null){
            avarias.remove(l);
        }
    }
}
