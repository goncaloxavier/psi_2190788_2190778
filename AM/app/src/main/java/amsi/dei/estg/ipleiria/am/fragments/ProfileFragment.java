package amsi.dei.estg.ipleiria.am.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;

public class ProfileFragment extends Fragment {
    private TextView tvUsername;
    private Button btnMyAnomaly;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        tvUsername = rootview.findViewById(R.id.textView3);
        btnMyAnomaly = rootview.findViewById(R.id.btnMyAnomaly);
        Utilizador utilizador = SingletonGestorAvarias.getInstance(getContext()).getUtilizador();
        tvUsername.setText(utilizador.getNomeUtilizador());

        return rootview;
    }
}