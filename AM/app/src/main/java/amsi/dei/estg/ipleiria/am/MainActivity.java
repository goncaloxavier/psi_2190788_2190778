package amsi.dei.estg.ipleiria.am;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import amsi.dei.estg.ipleiria.am.fragments.ListaAvariasFragment;
import amsi.dei.estg.ipleiria.am.fragments.ListaDispositivosFragment;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;
import amsi.dei.estg.ipleiria.am.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationView2;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView2 = (BottomNavigationView)findViewById(R.id.navigation_tecnico);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView2.setOnNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        verifyUser(SingletonGestorAvarias.getInstance(getApplicationContext()).getUtilizador());
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.dispositivos:
                fragment = new ListaDispositivosFragment();
                break;
            case R.id.minhasAvarias:
                fragment = new ListaAvariasFragment();
                break;
            case R.id.listaAvarias:
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

    private void verifyUser(Utilizador utilizador){
        Fragment fragment = null;
        if(utilizador != null){
            if(utilizador.getTipo() != 0){
                bottomNavigationView2.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                fragment = new ListaAvariasFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                bottomNavigationView2.setSelectedItemId(R.id.listaAvarias);
            }else{
                bottomNavigationView2.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                fragment = new ListaAvariasFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                bottomNavigationView.setSelectedItemId(R.id.minhasAvarias);
            }
        }
    }
}