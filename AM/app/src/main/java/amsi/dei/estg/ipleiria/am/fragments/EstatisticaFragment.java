package amsi.dei.estg.ipleiria.am.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import amsi.dei.estg.ipleiria.am.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstatisticaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstatisticaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView txtAvarias, txtPecas, txtAvariasRes, txtAvariasNaoRes, txtDispositivos, txtDispositivosNaoF;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public EstatisticaFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstatisticaFragment newInstance(String param1, String param2) {
        EstatisticaFragment fragment = new EstatisticaFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        txtAvarias = getView().findViewById(R.id.txt_NumAvarias);
        txtPecas = getView().findViewById(R.id.txt_NumPecas);
        txtAvariasRes = getView().findViewById(R.id.txt_NumAvariasResolvidas);
        txtAvariasNaoRes = getView().findViewById(R.id.txt_NumAvariasNaoResolvidas);
        txtDispositivos = getView().findViewById(R.id.txt_NumDispositivosFuncionais);
        txtDispositivosNaoF = getView().findViewById(R.id.txt_NumDispostivosNaoFuncionais);
/*
        txtAvarias.setText();
        txtPecas.setText();
        txtAvariasRes.setText();
        txtAvariasNaoRes.setText();
        txtDispositivos.setText();
        txtDispositivosNaoF.setText();
        */

        return inflater.inflate(R.layout.fragment_estatistica, container, false);

    }

}