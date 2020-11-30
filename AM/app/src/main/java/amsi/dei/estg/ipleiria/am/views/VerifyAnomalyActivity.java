package amsi.dei.estg.ipleiria.am.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import amsi.dei.estg.ipleiria.am.R;

public class VerifyAnomalyActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_anomaly);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.verificar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.verificar:
                        break;
                    case R.id.avaria:
                        startActivity(new Intent(getApplicationContext(), AnomalyActivity.class));
                        finish();
                        break;
                    case R.id.minhasAvarias:
                        startActivity(new Intent(getApplicationContext(), AnomalyListActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        break;
                }
                return true;
            }
        });
    }
}