package amsi.dei.estg.ipleiria.am.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import amsi.dei.estg.ipleiria.am.MainActivity;
import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.listeners.VolleyListener;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;

public class LoginActivity extends AppCompatActivity implements VolleyListener {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private Utilizador utilizador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SingletonGestorAvarias.getInstance(getApplicationContext()).setVolleyListener(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    public void onClickLogin(View view) {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        SingletonGestorAvarias.getInstance(getApplicationContext()).loginAPI(username, password, getApplicationContext());
        (new Handler()).postDelayed(this::printUser, 2000);
    }


    @Override
    public void requestFinished(boolean exsitance) {
        if(exsitance == true){
            utilizador = SingletonGestorAvarias.getInstance(getApplicationContext()).getUtilizador();
        }
    }

    public void printUser(){
        if(utilizador != null){
            //SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
            //SingletonGestorAvarias.getInstance(getApplicationContext()).saveUser(sharedPreferences);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}