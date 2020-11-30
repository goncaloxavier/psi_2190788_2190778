package amsi.dei.estg.ipleiria.am;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.adaptors.ListaAvariasAdaptor;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.views.AnomalyActivity;

public class ListaAvariasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lvListaAvarias;
    private ArrayList<Avaria> listaAvarias;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootview = inflater.inflate(R.layout.fragment_lista_avarias, container, false);

        listaAvarias = SingletonGestorAvarias.getInstance(getContext()).getAvariasDB();
        lvListaAvarias = rootview.findViewById(R.id.lvListaAvarias);
        lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(),listaAvarias));

        lvListaAvarias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Avaria temAvaria = (Avaria) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), AnomalyActivity.class);
                intent.putExtra(AnomalyActivity.AVARIA, temAvaria.getIdAvaria());
                startActivityForResult(intent, AnomalyActivity.EDITAR);
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
                    listaAvarias = SingletonGestorAvarias.getInstance(getContext()).getAvariasDB();
                    lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(), listaAvarias));
                    Snackbar.make(getView(), "Avaria adicionada com sucesso", Snackbar.LENGTH_LONG).show();
                    break;
                case AnomalyActivity.EDITAR:
                    listaAvarias = SingletonGestorAvarias.getInstance(getContext()).getAvariasDB();
                    lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(), listaAvarias));
                    Snackbar.make(getView(), "Avaria modificada com sucesso", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        listaAvarias = SingletonGestorAvarias.getInstance(getContext()).getAvariasDB();
        lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getContext(), listaAvarias));
        swipeRefreshLayout.setRefreshing(false);
    }
}