package amsi.dei.estg.ipleiria.am.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import amsi.dei.estg.ipleiria.am.MainActivity;
import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private SingletonGestorAvarias singletonGestorAvarias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        singletonGestorAvarias = new SingletonGestorAvarias(getApplicationContext());

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    public void onClickLogin(View view) {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
     ///   singletonGestorAvarias.loginA(username,password,getApplicationContext());

        System.out.println("--> Username: " + username + "  Password: " + password);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}