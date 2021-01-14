package amsi.dei.estg.ipleiria.am.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.models.Estatistica;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;

public class EstatisticaFragment extends Fragment {

    private Estatistica estatistica;
    private TextView txtAvarias, txtDispositivos, txtAvariasRes, txtAvariasNaoRes, txtDispositivosF, txtDispositivosNaoF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_estatistica, container, false);

        txtAvarias = rootview.findViewById(R.id.txt_NumAvarias);

        txtAvariasRes = rootview.findViewById(R.id.txt_NumAvariasResolvidas);
        txtAvariasNaoRes = rootview.findViewById(R.id.txt_NumAvariasNaoResolvidas);
        txtDispositivosF = rootview.findViewById(R.id.txt_NumDispositivosFuncionais);
        txtDispositivosNaoF = rootview.findViewById(R.id.txt_NumDispostivosNaoFuncionais);

        SingletonGestorAvarias.getInstance(getContext()).setEstatisticaAPI(getContext());
        estatistica = SingletonGestorAvarias.getInstance(getContext()).getEstatistica();

        carregarEstatistica();
        return rootview;
    }

    public void carregarEstatistica(){
        if(estatistica != null){
            txtAvarias.setText(String.valueOf(estatistica.getNumAvarias()));
            txtAvariasRes.setText(String.valueOf(estatistica.getNumAvariasR()));
            txtAvariasNaoRes.setText(String.valueOf(estatistica.getNumAvariasNR()));
            txtDispositivosF.setText(String.valueOf(estatistica.getNumDispositivosF()));
            txtDispositivosNaoF.setText(String.valueOf(estatistica.getNumDispositivosNF()));
        }
    }
}