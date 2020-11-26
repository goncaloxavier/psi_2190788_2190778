package amsi.dei.estg.ipleiria.am.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import amsi.dei.estg.ipleiria.am.R;

public class HomeActivity extends AppCompatActivity {

    private Button btnAnomaly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAnomaly = findViewById(R.id.btnAnomaly);
    }

    public void onClickAvaria(View view) {
        Intent intent = new Intent(this, AnomalyActivity.class);
        startActivity(intent);
    }
}