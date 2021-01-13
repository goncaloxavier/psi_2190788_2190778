package amsi.dei.estg.ipleiria.am.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import amsi.dei.estg.ipleiria.am.MainActivity;
import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;
import amsi.dei.estg.ipleiria.am.views.AnomalyActivity;
import amsi.dei.estg.ipleiria.am.views.MyAnomalyListActivityActivity;

public class ProfileFragment extends Fragment {
    private TextView tvUsername;
    private Button btnMyAnomaly;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        tvUsername = rootview.findViewById(R.id.textView3);
        btnMyAnomaly = rootview.findViewById(R.id.btnMyAnomaly);
        Utilizador utilizador = SingletonGestorAvarias.getInstance(getContext()).getUtilizador();
        tvUsername.setText(utilizador.getNomeUtilizador());

        btnMyAnomaly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity mainActivity = (MainActivity) getActivity();
                Intent intent = new Intent(mainActivity.getApplicationContext(), MyAnomalyListActivityActivity.class);
                startActivityForResult(intent, MyAnomalyListActivityActivity.COMEBACK);
            }
        });
        return rootview;
    }
}