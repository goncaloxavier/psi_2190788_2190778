package amsi.dei.estg.ipleiria.am;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import amsi.dei.estg.ipleiria.am.adaptors.ListaAvariasAdaptor;
import amsi.dei.estg.ipleiria.am.fragments.EstatisticaFragment;
import amsi.dei.estg.ipleiria.am.fragments.ListaAvariasFragment;
import amsi.dei.estg.ipleiria.am.fragments.VerificarAvariasFragment;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;
import amsi.dei.estg.ipleiria.am.views.AnomalyActivity;
import amsi.dei.estg.ipleiria.am.fragments.ProfileFragment;
import amsi.dei.estg.ipleiria.am.views.MyAnomalyListActivityActivity;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationView2;

    private FragmentManager fragmentManager;
    private int whichFragment;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView2 = (BottomNavigationView)findViewById(R.id.navigation_tecnico);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView2.setOnNavigationItemSelectedListener(this);
        verifyUser(SingletonGestorAvarias.getInstance(getApplicationContext()).getUtilizador());
        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.estatistica:
                fragment = new EstatisticaFragment();
                break;
            case R.id.verificarAvaria:
                fragment = new VerificarAvariasFragment();
                break;
            case R.id.minhasAvarias:
                fragment = new ListaAvariasFragment();
                break;
            case R.id.profile:
                fragment = new ProfileFragment();
                break;
        }
        if(fragment != null){
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
        return true;
    }

    public void carregarFragmentoInicial(){
        Fragment fragment = new ListaAvariasFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        bottomNavigationView.setSelectedItemId(R.id.minhasAvarias);
    }

    private void verifyUser(Utilizador utilizador){
        switch (utilizador.getTipo()){
            case 0:
                bottomNavigationView2.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                break;
            case 1:
                bottomNavigationView2.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                bottomNavigationView2.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                break;
        }
    }
}