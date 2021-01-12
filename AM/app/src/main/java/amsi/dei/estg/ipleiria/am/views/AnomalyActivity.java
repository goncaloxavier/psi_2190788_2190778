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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.Dispositivo;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;

public class AnomalyActivity extends AppCompatActivity  {

    public static final String AVARIA = "avaria";
    public static final int ADICIONAR = 1;
    public static final int EDITAR = 2;
    private int idAvaria, positionE, positionD;
    private Avaria avaria;
    private ArrayList<Dispositivo> dispositivos;
    private TextView tvEstadoEdit;
    private EditText edtDescricao;
    private CheckBox cbHardware, cbSoftware, cbFuncional, cbNFuncional;
    private Spinner spinnerEstado, spinnerDispositivo;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anomaly);

        idAvaria = getIntent().getIntExtra(AVARIA, -1);
        SingletonGestorAvarias.getInstance(getApplicationContext()).getAvariasDB();
        avaria = SingletonGestorAvarias.getInstance(getApplicationContext()).getAvaria(idAvaria);
        dispositivos = SingletonGestorAvarias.getInstance(getApplicationContext()).getDispositivosDB();

        edtDescricao = findViewById(R.id.mvDescricao);
        cbHardware = findViewById(R.id.cbHardware);
        cbSoftware = findViewById(R.id.cbSoftware);
        cbFuncional = findViewById(R.id.cbFuncional);
        cbNFuncional = findViewById(R.id.cbNFuncional);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        spinnerDispositivo = findViewById(R.id.spinnerDispositivo);
        tvEstadoEdit = findViewById(R.id.tvEstadoEdit);
        fab = findViewById(R.id.fab_avaria);


        spinnerDispositivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                positionD = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                positionE = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if(avaria != null){

            edtDescricao.setText(avaria.getDescricao());
            //edtIdDispositivo.setText(String.valueOf(avaria.getIdDispositivo()));
            if(avaria.getTipo() == 0){
                cbHardware.setChecked(true);
                cbSoftware.setChecked(false);
            }else{
                cbHardware.setChecked(false);
                cbSoftware.setChecked(true);
            }
            if(avaria.getGravidade() == 1){
                cbFuncional.setChecked(true);
                cbNFuncional.setChecked(false);
            }else{
                cbFuncional.setChecked(false);
                cbNFuncional.setChecked(true);
            }
            fab.setImageResource(R.drawable.ic_guardar_foreground);
            spinnerDispositivo.setVisibility(View.VISIBLE);
            spinnerEstado.setVisibility(View.VISIBLE);
            setSpinnerEstado();
            setSpinnerDispositivo();
        }
        else{
            setTitle("Adicionar Avaria");
            tvEstadoEdit.setVisibility(View.GONE);
            spinnerEstado.setVisibility(View.GONE);
            spinnerDispositivo.setVisibility(View.VISIBLE);
            setSpinnerDispositivo();
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
                    setSpinnerEstado();
                    setSpinnerDispositivo();
                    avaria.setEstado(positionE);
                    System.out.println("MEKIE->"+positionD);
                    avaria.setIdDispositivo(positionD + 1);
                    SingletonGestorAvarias.getInstance(getApplicationContext()).editarAvariaAPI(avaria, getApplicationContext());
                }else{
                    setSpinnerDispositivo();
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

                    Date currentTime = Calendar.getInstance().getTime();
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
                    String sdt = df.format(currentTime);
                    System.out.println("MEKIE->"+positionD);
                    positionD = positionD + 1;
                    System.out.println("MEKIEAFTER->"+positionD);
                    Avaria auxAvaria = new Avaria(0, 0, gravidade,  tipo, positionD, sdt, edtDescricao.getText().toString(), 1);

                    SingletonGestorAvarias.getInstance(getApplicationContext()).adicionarAvariaAPI(auxAvaria, getApplicationContext());
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
                        SingletonGestorAvarias.getInstance(getApplicationContext()).removerAvariaAPI(avaria, getApplicationContext());
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

    public void setSpinnerEstado(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setVisibility(View.VISIBLE);
        spinnerEstado.setSelection(avaria.getEstado());
    }

    public void setSpinnerDispositivo(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDispositivo.setAdapter(spinnerAdapter);
        for (Dispositivo dispositivo:dispositivos) {
            spinnerAdapter.add(dispositivo.getReferencia());
            System.out.println("HEY->"+dispositivo.getReferencia());
        }
        spinnerAdapter.notifyDataSetChanged();
        if(avaria != null){
            spinnerDispositivo.setSelection(avaria.getIdDispositivo());
        }
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
}