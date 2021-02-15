package amsi.dei.estg.ipleiria.am;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import amsi.dei.estg.ipleiria.am.fragments.EstatisticaFragment;
import amsi.dei.estg.ipleiria.am.fragments.ListaAvariasFragment;
import amsi.dei.estg.ipleiria.am.fragments.ListaDispositivosFragment;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;
import amsi.dei.estg.ipleiria.am.fragments.ProfileFragment;
import amsi.dei.estg.ipleiria.am.utils.AvariaJsonParser;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MqttCallback {

    private static final String CHANNEL_ID = "anomalyChannel";
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationView2;
    private Utilizador auxUtilizador;

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


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("anomalyChannel", "anomalyChannel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.dispositivos:
                fragment = new ListaDispositivosFragment();
                break;
            case R.id.estatistica:
                fragment = new EstatisticaFragment();
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
        auxUtilizador = utilizador;
        if(utilizador != null){
            if(utilizador.getTipo() != 0){
                bottomNavigationView2.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                fragment = new ListaAvariasFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                bottomNavigationView2.setSelectedItemId(R.id.listaAvarias);
                SingletonGestorAvarias.getInstance(getApplicationContext()).setMqttCallback(this);
            }else{
                bottomNavigationView2.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                fragment = new ListaAvariasFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                bottomNavigationView.setSelectedItemId(R.id.minhasAvarias);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());

        Avaria avariaNotif = AvariaJsonParser.parserJsonAvaria(payload);
        String idAvaria = String.valueOf(avariaNotif.getIdAvaria());
        String descricao = avariaNotif.getDescricao();
        String autor = SingletonGestorAvarias.getInstance(getApplicationContext()).getUtilizador(avariaNotif.getIdUtilizador()).getNomeUtilizador();
        String dispositivo = SingletonGestorAvarias.getInstance(getApplicationContext()).getDispositivo(avariaNotif.getIdDispositivo()).getReferencia();

        if(auxUtilizador.getIdUtilizador() != avariaNotif.getIdUtilizador()){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_error_foreground)
                    .setContentTitle(autor + " " + dispositivo)
                    .setContentText(descricao)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            managerCompat.notify(1, builder.build());
        }
        SingletonGestorAvarias.getInstance(getApplicationContext()).getAllAvariasAPI(getApplicationContext());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}