package amsi.dei.estg.ipleiria.am.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.adaptors.ListaDispositivosAdaptor;
import amsi.dei.estg.ipleiria.am.listeners.DispositivoListener;
import amsi.dei.estg.ipleiria.am.models.Dispositivo;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;

public class ListaDispositivosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DispositivoListener {

    private ListView lvListaDispositivos;
    private ArrayList<Dispositivo> dispositivos;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootview = inflater.inflate(R.layout.fragment_lista_dispositivos, container, false);

        lvListaDispositivos = rootview.findViewById(R.id.lvListaDispositivos);

        SingletonGestorAvarias.getInstance(getContext()).setDispositivoListener(this);
        SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());

        lvListaDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        swipeRefreshLayout = rootview.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        return rootview;
    }

    @Override
    public void onRefresh() {
        SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onDispositivosRefresh(ArrayList<Dispositivo> dispositivo) {
        if(dispositivo != null){
            lvListaDispositivos.setAdapter(new ListaDispositivosAdaptor(getContext(), dispositivo));
        }
    }
}