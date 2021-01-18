package amsi.dei.estg.ipleiria.am.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amsi.dei.estg.ipleiria.am.MainActivity;
import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.listeners.LoginListener;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;

public class LoginActivity extends AppCompatActivity implements LoginListener {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SingletonGestorAvarias.getInstance(getApplicationContext()).setLoginListener(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        if(SingletonGestorAvarias.getInstance(getApplicationContext()).getUtilizador() == null){
            edtUsername.setText("");
            edtPassword.setText("");
        }
    }

    public void onClickLogin(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        SingletonGestorAvarias.getInstance(getApplicationContext()).loginAPI(username, password, getApplicationContext(), sharedPreferences);
    }

    @Override
    public void validateLogin(Utilizador utilizador) {
        if(utilizador != null){
            SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            SingletonGestorAvarias.getInstance(getApplicationContext()).saveUsertoSharedPref(sharedPreferences, utilizador);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            edtUsername.setError("Wrong Username");
            edtPassword.setError("Wrong Password");
            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

    }
}