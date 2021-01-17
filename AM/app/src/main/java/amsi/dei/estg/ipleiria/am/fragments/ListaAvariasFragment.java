package amsi.dei.estg.ipleiria.am.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.adaptors.ListaAvariasAdaptor;
import amsi.dei.estg.ipleiria.am.listeners.AvariasListener;
import amsi.dei.estg.ipleiria.am.listeners.DispositivoListener;
import amsi.dei.estg.ipleiria.am.listeners.UtilizadorListener;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.Dispositivo;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;
import amsi.dei.estg.ipleiria.am.utils.AvariaJsonParser;
import amsi.dei.estg.ipleiria.am.views.AnomalyActivity;

public class ListaAvariasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AvariasListener, UtilizadorListener, DispositivoListener {

    private ListView lvListaAvarias;
    private ArrayList<Utilizador> utilizadores;
    private ArrayList<Dispositivo> dispositivos;
    private ArrayList<Avaria> listaAvarias;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootview = inflater.inflate(R.layout.fragment_lista_avarias, container, false);

        lvListaAvarias = rootview.findViewById(R.id.lvListaAvarias);
        fab = rootview.findViewById(R.id.fab_avaria);

        SingletonGestorAvarias.getInstance(getContext()).setAvariasListener(this);
        SingletonGestorAvarias.getInstance(getContext()).setDispositivoListener(this);
        SingletonGestorAvarias.getInstance(getContext()).setUtilizadorListener(this);

        if(SingletonGestorAvarias.getInstance(getContext()).getUtilizador().getTipo() != 0){
            SingletonGestorAvarias.getInstance(getContext()).getAllUsersAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllAvariasAPI(getContext());
        }else{
            SingletonGestorAvarias.getInstance(getContext()).getAllUsersAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllAvariasUserAPI(getContext());
        }

        lvListaAvarias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Avaria temAvaria = (Avaria) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), AnomalyActivity.class);
                intent.putExtra(AnomalyActivity.AVARIA, temAvaria.getIdAvaria());
                startActivityForResult(intent, AnomalyActivity.EDITAR);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AnomalyActivity.class);
                startActivityForResult(intent, AnomalyActivity.ADICIONAR);
            }
        });

        swipeRefreshLayout = rootview.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case AnomalyActivity.ADICIONAR:
                    if(SingletonGestorAvarias.getInstance(getContext()).getUtilizador().getTipo() != 0){
                        SingletonGestorAvarias.getInstance(getContext()).getAllAvariasAPI(getContext());
                    }else{
                        SingletonGestorAvarias.getInstance(getContext()).getAllAvariasUserAPI(getContext());
                    }
                    lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(), listaAvarias));
                    Snackbar.make(getView(), "Avaria adicionada com sucesso", Snackbar.LENGTH_LONG).show();
                    break;
                case AnomalyActivity.EDITAR:
                    if(SingletonGestorAvarias.getInstance(getContext()).getUtilizador().getTipo() != 0){
                        SingletonGestorAvarias.getInstance(getContext()).getAllAvariasAPI(getContext());
                    }else{
                        SingletonGestorAvarias.getInstance(getContext()).getAllAvariasUserAPI(getContext());
                    }
                    lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(), listaAvarias));
                    Snackbar.make(getView(), "Avaria modificada com sucesso", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);

        MenuItem itemPesquisa = menu.findItem(R.id.itemPesquisar);
        searchView = (SearchView) itemPesquisa.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());
                SingletonGestorAvarias.getInstance(getContext()).getAllUsersAPI(getContext());
                SingletonGestorAvarias.getInstance(getContext()).getDispositivobyRef(getContext(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        if(SingletonGestorAvarias.getInstance(getContext()).getUtilizador().getTipo() != 0){
            SingletonGestorAvarias.getInstance(getContext()).getAllUsersAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllAvariasAPI(getContext());
        }else{
            SingletonGestorAvarias.getInstance(getContext()).getAllUsersAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());
            SingletonGestorAvarias.getInstance(getContext()).getAllAvariasUserAPI(getContext());
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefreshListaAvarias(ArrayList<Avaria> listaAvaria) {
        if(listaAvaria != null){
            lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(), listaAvaria));
        }else{
            lvListaAvarias.setAdapter(null);
        }
    }

    @Override
    public void onUpdateListaAvarias(Avaria avaria, ArrayList avarias, int operacao) {

    }

    @Override
    public void onDispositivosRefresh(ArrayList<Dispositivo> dispositivo) {

    }

    @Override
    public void onUtilizadoresRefresh(ArrayList<Utilizador> utilizador) {

    }
}