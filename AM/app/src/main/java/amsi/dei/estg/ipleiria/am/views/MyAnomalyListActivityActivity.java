package amsi.dei.estg.ipleiria.am.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.MainActivity;
import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.adaptors.ListaAvariasAdaptor;
import amsi.dei.estg.ipleiria.am.listeners.AvariasListener;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;

public class MyAnomalyListActivityActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AvariasListener {
    private ListView lvListaAvarias;
    private ArrayList<Avaria> listaAvarias;
    public static final int COMEBACK = 1;
    SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_anomaly_list_activity);

        lvListaAvarias = findViewById(R.id.lvListaAvarias);
        fab = findViewById(R.id.fab_avaria);

        SingletonGestorAvarias.getInstance(getApplicationContext()).setAvariasListener(this);
        SingletonGestorAvarias.getInstance(getApplicationContext()).getAllAvariasUserAPI(getApplicationContext());
        SingletonGestorAvarias.getInstance(getApplicationContext()).getAllDispositivosAPI(getApplicationContext());

        lvListaAvarias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Avaria temAvaria = (Avaria) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), AnomalyActivity.class);
                intent.putExtra(AnomalyActivity.AVARIA, temAvaria.getIdAvaria());
                startActivityForResult(intent, AnomalyActivity.EDITAR);
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AnomalyActivity.ADICIONAR:
                    SingletonGestorAvarias.getInstance(getApplicationContext()).getAllAvariasAPI(getApplicationContext());
                    lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getApplicationContext(), listaAvarias));
                    Snackbar.make(this.getCurrentFocus(), "Avaria adicionada com sucesso", Snackbar.LENGTH_LONG).show();
                    break;
                case AnomalyActivity.EDITAR:
                    SingletonGestorAvarias.getInstance(getApplicationContext()).getAllAvariasAPI(getApplicationContext());
                    lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getApplicationContext(), listaAvarias));
                    Snackbar.make(this.getCurrentFocus(), "Avaria modificada com sucesso", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        SingletonGestorAvarias.getInstance(getApplicationContext()).getAllAvariasUserAPI(getApplicationContext());
        SingletonGestorAvarias.getInstance(getApplicationContext()).getAllDispositivosAPI(getApplicationContext());

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefreshListaAvarias(ArrayList<Avaria> listaAvaria) {
        if(listaAvaria != null){
            lvListaAvarias.setAdapter(new ListaAvariasAdaptor(getApplicationContext(), listaAvaria));
        }
    }

    @Override
    public void onUpdateListaAvarias(Avaria avaria, ArrayList avarias, int operacao) {

    }
}