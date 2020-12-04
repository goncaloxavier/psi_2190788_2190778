package amsi.dei.estg.ipleiria.am.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;

public class AnomalyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String AVARIA = "avaria";
    public static final int ADICIONAR = 1;
    public static final int EDITAR = 2;
    private int idAvaria, spinnerPosition;
    private Avaria avaria;
    private TextView tvEstadoEdit;
    private EditText edtDescricao, edtIdDispositivo;
    private CheckBox cbHardware, cbSoftware, cbFuncional, cbNFuncional;
    private Spinner spinnerEstado;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anomaly);

        idAvaria = getIntent().getIntExtra(AVARIA, -1);
        avaria = SingletonGestorAvarias.getInstance(getApplicationContext()).getAvaria(idAvaria);

        edtDescricao = findViewById(R.id.mvDescricao);
        edtIdDispositivo = findViewById(R.id.edtIdDispositivo);
        cbHardware = findViewById(R.id.cbHardware);
        cbSoftware = findViewById(R.id.cbSoftware);
        cbFuncional = findViewById(R.id.cbFuncional);
        cbNFuncional = findViewById(R.id.cbNFuncional);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        tvEstadoEdit = findViewById(R.id.tvEstadoEdit);
        fab = findViewById(R.id.fab_avaria);

        if(avaria != null){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.estados, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEstado.setAdapter(adapter);
            spinnerEstado.setOnItemSelectedListener(this);
            spinnerEstado.setVisibility(View.VISIBLE);
            spinnerEstado.setSelection(avaria.getEstado());
            edtDescricao.setText(avaria.getDescricao());
            edtIdDispositivo.setText(String.valueOf(avaria.getIdDispositivo()));
            if(avaria.getTipo() == 0){
                cbHardware.setChecked(true);
                cbSoftware.setChecked(false);
            }else{
                cbHardware.setChecked(false);
                cbSoftware.setChecked(true);
            }
            if(avaria.getGravidade() == 0){
                cbFuncional.setChecked(true);
                cbNFuncional.setChecked(false);
            }else{
                cbFuncional.setChecked(false);
                cbNFuncional.setChecked(true);
            }
            fab.setImageResource(R.drawable.ic_guardar_foreground);
        }
        else{
            setTitle("Adicionar Avaria");
            tvEstadoEdit.setVisibility(View.GONE);
            spinnerEstado.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_adicionar_foreground);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avaria != null){
                    avaria.setDescricao(edtDescricao.getText().toString());
                    if(cbFuncional.isChecked()){
                        avaria.setGravidade(0);
                    }else{
                        avaria.setGravidade(1);
                    }
                    if(cbHardware.isChecked()){
                        avaria.setTipo(0);
                    }else{
                        avaria.setTipo(1);
                    }
                    avaria.setEstado(spinnerPosition);
                    avaria.setIdDispositivo(Integer.parseInt(edtIdDispositivo.getText().toString()));
                    SingletonGestorAvarias.getInstance(getApplicationContext()).editarAvariaDB(avaria);
                }else{
                    int gravidade = 0;
                    int tipo = 0;
                    if(cbFuncional.isChecked()){
                        gravidade = 1;
                    }else{
                        gravidade = 0;
                    }
                    if(cbHardware.isChecked()){
                       tipo = 0;
                    }else{
                        tipo = 1;
                    }
                    Avaria auxAvaria = new Avaria(0, gravidade, tipo,
                            Integer.parseInt(edtIdDispositivo.getText().toString()), "2020",
                            edtDescricao.getText().toString());

                    SingletonGestorAvarias.getInstance(getApplicationContext()).adicionarAvariaDB(auxAvaria);
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(avaria != null){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_avaria, menu);
            return super.onCreateOptionsMenu(menu);
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemRemover:
                dialogRemover();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogRemover(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover Avaria")
                .setMessage("Pretende mesmo remover a avaria?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonGestorAvarias.getInstance(getApplicationContext()).removerAvariaDB(avaria.getIdAvaria());
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    public void onClickNFuncional(View view) {
        cbFuncional.setChecked(false);
    }

    public void onClickHardware(View view) {
        cbSoftware.setChecked(false);
    }

    public void onClickSoftware(View view) {
        cbHardware.setChecked(false);
    }

    public void onClickFuncional(View view) {
        cbNFuncional.setChecked(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}