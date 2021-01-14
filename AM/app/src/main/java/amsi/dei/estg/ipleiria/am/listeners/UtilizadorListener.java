package amsi.dei.estg.ipleiria.am.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.models.Utilizador;

public interface UtilizadorListener {
    public void onUtilizadoresRefresh(ArrayList<Utilizador> utilizador);
}
