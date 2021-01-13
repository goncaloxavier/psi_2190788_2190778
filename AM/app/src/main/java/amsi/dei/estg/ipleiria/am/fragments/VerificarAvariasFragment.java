package amsi.dei.estg.ipleiria.am.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.adaptors.ListaAvariasAdaptor;
import amsi.dei.estg.ipleiria.am.listeners.AvariasListener;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.views.AnomalyActivity;

public class VerificarAvariasFragment extends Fragment{

    private ListView lvListaAvarias;
    private ArrayList<Avaria> listaAvarias;
    SearchView searchView;
    private FloatingActionButton fab;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootview = inflater.inflate(R.layout.fragment_lista_avarias, container, false);
        fab = rootview.findViewById(R.id.fab_avaria);
        fab.setVisibility(View.INVISIBLE);
        lvListaAvarias = rootview.findViewById(R.id.lvListaAvarias);
        swipeRefreshLayout = rootview.findViewById(R.id.swipe);
        swipeRefreshLayout.setEnabled(false);


        SingletonGestorAvarias.getInstance(getContext()).getAllDispositivosAPI(getContext());

        lvListaAvarias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Avaria temAvaria = (Avaria) parent.getItemAtPosition(position);
                System.out.println("JAVARDAO " + temAvaria.getIdAvaria());
                Intent intent = new Intent(getContext(), AnomalyActivity.class);
                intent.putExtra(AnomalyActivity.AVARIA, temAvaria.getIdAvaria());
                startActivityForResult(intent, AnomalyActivity.EDITAR);
            }
        });

        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case AnomalyActivity.EDITAR:
                    SingletonGestorAvarias.getInstance(getContext()).getAllAvariasAPI(getContext());
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
                SingletonGestorAvarias.getInstance(getContext()).getDispositivobyRef(getContext(), query);
                listaAvarias = SingletonGestorAvarias.getInstance(getContext()).getAvarias();
                lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(), listaAvarias));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}