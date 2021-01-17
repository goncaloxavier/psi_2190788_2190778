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
    private TextView tvUsername, tvEmail, tvNumAvarias;
    private Button btnLogout;
    private Utilizador utilizador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        tvUsername = rootview.findViewById(R.id.textView3);
        tvEmail = rootview.findViewById(R.id.tvEmail);
        tvNumAvarias = rootview.findViewById(R.id.tvNumAvarias);
        utilizador = SingletonGestorAvarias.getInstance(getContext()).getUtilizador();

        tvUsername.setText(utilizador.getNomeUtilizador());
        tvEmail.setText(utilizador.getEmail());
        tvNumAvarias.setText(Integer.toString(SingletonGestorAvarias.getInstance(getContext()).getNumAvariasByUser(utilizador.getIdUtilizador())));

        return rootview;
    }
}