package amsi.dei.estg.ipleiria.am.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.models.Avaria;

public interface AvariasListener {
    void onRefreshListaAvarias(ArrayList<Avaria> listaAvaria);
    void onUpdateListaAvarias(Avaria avaria, int operacao);
}
