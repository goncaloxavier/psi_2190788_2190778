package amsi.dei.estg.ipleiria.am.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.models.Dispositivo;

public interface DispositivoListener {
    public void onDispositivosRefresh(ArrayList<Dispositivo> dispositivo);
}
