package amsi.dei.estg.ipleiria.am;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import amsi.dei.estg.ipleiria.am.fragments.EstatisticaFragment;
import amsi.dei.estg.ipleiria.am.fragments.ListaAvariasFragment;
import amsi.dei.estg.ipleiria.am.views.AnomalyActivity;
import amsi.dei.estg.ipleiria.am.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.estatistica:
                fragment = new EstatisticaFragment();
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

    private void carregarFragmentoInicial(){
        bottomNavigationView.setSelectedItemId(R.id.minhasAvarias);
        Fragment fragment = new ListaAvariasFragment();
        if(fragment != null){
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}