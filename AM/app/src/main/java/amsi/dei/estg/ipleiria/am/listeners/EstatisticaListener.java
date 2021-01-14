package amsi.dei.estg.ipleiria.am.listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.models.Estatistica;
import amsi.dei.estg.ipleiria.am.models.Utilizador;

public interface EstatisticaListener {
    public void onEstatisticaRefresh(Estatistica estatistica);
}
