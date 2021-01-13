package amsi.dei.estg.ipleiria.am.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bumptech.glide.util.Util;

import java.util.ArrayList;

public class AvariaDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "amDB";
    private static final String TABLE_NAME_AVARIA = "Avaria";
    private static final String TABLE_NAME_DISPOSITIVO = "Dispositivo";
    private static final String TABLE_NAME_UTILIZADOR = "Utilizador";

    //AVARIA
    private static final String ID_AVARIA = "id";
    private static final String DESCRICAO_AVARIA = "descricao";
    private static final String TIPO_AVARIA = "tipo";
    private static final String ESTADO_AVARIA = "estado";
    private static final String DATA_AVARIA = "dataAvaria";
    private static final String DISPOSITIVO_AVARIA = "idDispositivo";
    private static final String UTILIZADOR_AVARIA = "idUtilizador";
    private static final String GRAVIDADE_AVARIA = "gravidade";


    //DISPOSITIVO
    private static final String ID_DISPOSITIVO = "id";
    private static final String REFERENCIA_DISPOSITIVO = "referencia";
    private static final String TIPO_DISPOSITIVO = "tipo";
    private static final String ESTADO_DISPOSITIVO = "estado";
    private static final String DATA_DISPOSITIVO = "dataCompra";

    //UTILIZADOR
    private static final String ID_UTILIZADOR = "id";
    private static final String NOME_UTILIZADOR = "nomeUtilizador";
    private static final String PASS_UTILIZADOR = "palavraPasse";
    private static final String TIPO_UTILIZADOR = "tipo";
    private static final String ESTADO_UTILIZADOR = "estado";
    private static final String EMAIL_UTILIZADOR = "email";

    private final SQLiteDatabase sqLiteDatabase;

    public AvariaDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAvariaTable =
                "CREATE TABLE " + TABLE_NAME_AVARIA +
                        "(" + ID_AVARIA + " INTEGER PRIMARY KEY, " +
                        DESCRICAO_AVARIA + " TEXT NOT NULL, " +
                        ESTADO_AVARIA + " INTEGER NOT NULL, " +
                        TIPO_AVARIA + " INTEGER NOT NULL, " +
                        GRAVIDADE_AVARIA + " INTEGER NOT NULL, " +
                        DISPOSITIVO_AVARIA + " INTEGER NOT NULL, " +
                        UTILIZADOR_AVARIA + " INTEGER NOT NULL, " +
                        DATA_AVARIA + " TEXT NOT NULL" +
                        ");";

        String createDispositivoTable =
                "CREATE TABLE " + TABLE_NAME_DISPOSITIVO +
                        "(" + ID_DISPOSITIVO + " INTEGER PRIMARY KEY, " +
                        REFERENCIA_DISPOSITIVO + " TEXT NOT NULL, " +
                        TIPO_DISPOSITIVO + " TEXT NOT NULL, " +
                        ESTADO_DISPOSITIVO + " INTEGER NOT NULL, " +
                        DATA_DISPOSITIVO + " TEXT NOT NULL);";

        db.execSQL(createAvariaTable);
        db.execSQL(createDispositivoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AVARIA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DISPOSITIVO);

        this.onCreate(sqLiteDatabase);
    }

    //AVARIA
    public Avaria adicionarAvariaDB(Avaria avaria){
        ContentValues values = new ContentValues();
        values.put(ID_AVARIA, avaria.getIdAvaria());
        values.put(DESCRICAO_AVARIA, avaria.getDescricao());
        values.put(ESTADO_AVARIA, avaria.getEstado());
        values.put(TIPO_AVARIA, avaria.getTipo());
        values.put(GRAVIDADE_AVARIA, avaria.getGravidade());
        values.put(DISPOSITIVO_AVARIA, avaria.getIdDispositivo());
        values.put(UTILIZADOR_AVARIA, avaria.getIdUtilizador());
        values.put(DATA_AVARIA, String.valueOf(avaria.getDate()));

        long id = this.sqLiteDatabase.insert(TABLE_NAME_AVARIA, null, values);

        if(id > -1){
            avaria.setIdAvaria((int)id);
            return avaria;
        }

        return null;
    }

    public boolean editarAvariaDB(Avaria avaria){
        ContentValues values = new ContentValues();
        values.put(DESCRICAO_AVARIA, avaria.getDescricao());
        values.put(ESTADO_AVARIA, avaria.getEstado());
        values.put(TIPO_AVARIA, avaria.getTipo());
        values.put(GRAVIDADE_AVARIA, avaria.getGravidade());
        values.put(DISPOSITIVO_AVARIA, avaria.getIdDispositivo());
        values.put(UTILIZADOR_AVARIA, avaria.getIdUtilizador());
        values.put(DATA_AVARIA, String.valueOf(avaria.getDate()));

        return this.sqLiteDatabase.update(TABLE_NAME_AVARIA, values, "id = ?", new String[]{"" + avaria.getIdAvaria()}) > 0;
    }

    public boolean removerAvariaDB(int idAvaria){
        return (this.sqLiteDatabase.delete(TABLE_NAME_AVARIA, "id = ?", new String[]{"" + idAvaria}) == 1);
    }

    public void removerAllAvariasDB(){
        this.sqLiteDatabase.delete(TABLE_NAME_AVARIA, null, null);
    }

    public ArrayList<Avaria> getAllAvariasDB(){
        ArrayList<Avaria> avarias = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME_AVARIA, new String[]{
                        ID_AVARIA, ESTADO_AVARIA, TIPO_AVARIA, GRAVIDADE_AVARIA, DISPOSITIVO_AVARIA, DESCRICAO_AVARIA, DATA_AVARIA, UTILIZADOR_AVARIA},
                ESTADO_AVARIA + " IN (3,2,1,0)", null, null, null, ESTADO_AVARIA);

        if(cursor.moveToFirst()){
            do{
                Avaria auxAvaria = new Avaria(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3),
                        cursor.getInt(2), cursor.getInt(4), cursor.getString(6), cursor.getString(5),  cursor.getInt(7));

                avarias.add(auxAvaria);
            }while(cursor.moveToNext());
        }

        return avarias;
    }

    public ArrayList<Avaria> getAvariasByUserDB(int idUtilizador){
        ArrayList<Avaria> avarias = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME_AVARIA, new String[]{
                        ID_AVARIA, ESTADO_AVARIA, TIPO_AVARIA, GRAVIDADE_AVARIA, DISPOSITIVO_AVARIA, DESCRICAO_AVARIA, DATA_AVARIA, UTILIZADOR_AVARIA},
                ESTADO_AVARIA + " IN (3,2,1,0) " + ID_UTILIZADOR + " =?", new String[]{"" + idUtilizador}, null, null, ESTADO_AVARIA);

        if(cursor.moveToFirst()){
            do{
                Avaria auxAvaria = new Avaria(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3),
                        cursor.getInt(2), cursor.getInt(4), cursor.getString(6), cursor.getString(5),  cursor.getInt(7));

                avarias.add(auxAvaria);
            }while(cursor.moveToNext());
        }

        return avarias;
    }


    //DISPOSITIVOS
    public Dispositivo adicionarDispositivoDB(Dispositivo dispositivo){
        ContentValues values = new ContentValues();
        values.put(ID_DISPOSITIVO, dispositivo.getIdDispositivo());
        values.put(REFERENCIA_DISPOSITIVO, dispositivo.getReferencia());
        values.put(TIPO_DISPOSITIVO, dispositivo.getTipo());
        values.put(ESTADO_DISPOSITIVO, dispositivo.getEstado());
        values.put(DATA_DISPOSITIVO, dispositivo.getDataCompra());

        long id = this.sqLiteDatabase.insert(TABLE_NAME_DISPOSITIVO, null, values);

        if(id > -1){
            dispositivo.setIdDispositivo((int)id);
            return dispositivo;
        }

        return null;
    }

    public void removerAllDispositivosDB(){
        this.sqLiteDatabase.delete(TABLE_NAME_DISPOSITIVO, null, null);
    }

    public ArrayList<Dispositivo> getAllDispositivosDB(){
        ArrayList<Dispositivo> dispositivos = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME_DISPOSITIVO, new String[]{
                        ID_DISPOSITIVO, REFERENCIA_DISPOSITIVO, TIPO_DISPOSITIVO, ESTADO_DISPOSITIVO, DATA_DISPOSITIVO},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                Dispositivo auxDispositivo = new Dispositivo(cursor.getInt(0), cursor.getInt(3),
                        cursor.getString(4), cursor.getString(2), cursor.getString(1));

                dispositivos.add(auxDispositivo);
            }while(cursor.moveToNext());
        }

        return dispositivos;
    }
}