package amsi.dei.estg.ipleiria.am.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import amsi.dei.estg.ipleiria.am.R;

public class MenuTecnicoActivity extends AppCompatActivity {

    public static final String USERNAME = "USERNAME";
    private String username = "";
    private TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tecnico);

        tvUsername = findViewById(R.id.tvUsername);
        definirUsername();
    }

    private void definirUsername(){
        username = getIntent().getStringExtra(USERNAME);
        tvUsername.setText(username);
    }
}