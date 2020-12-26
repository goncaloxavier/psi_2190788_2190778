package amsi.dei.estg.ipleiria.am.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AvariaDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "amDB";
    private static final String TABLE_NAME = "Avaria";

    private static final String ID_AVARIA = "id";
    private static final String DESCRICAO_AVARIA = "descricao";
    private static final String TIPO_AVARIA = "tipo";
    private static final String ESTADO_AVARIA = "estado";
    private static final String DATA_AVARIA = "dataAvaria";
    private static final String DISPOSITIVO_AVARIA = "idDispositivo";
    private static final String GRAVIDADE_AVARIA = "gravidade";

    private final SQLiteDatabase sqLiteDatabase;

    public AvariaDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAvariaTable =
                "CREATE TABLE " + TABLE_NAME +
                        "(" + ID_AVARIA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DESCRICAO_AVARIA + " TEXT NOT NULL, " +
                        ESTADO_AVARIA + " INTEGER NOT NULL, " +
                        TIPO_AVARIA + " INTEGER NOT NULL, " +
                        GRAVIDADE_AVARIA + " INTEGER NOT NULL, " +
                        DISPOSITIVO_AVARIA + " INTEGER NOT NULL, " +
                        DATA_AVARIA + " TEXT NOT NULL" +
                        ");";
        db.execSQL(createAvariaTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public Avaria adicionarAvariaDB(Avaria avaria){
        ContentValues values = new ContentValues();
        values.put(DESCRICAO_AVARIA, avaria.getDescricao());
        values.put(ESTADO_AVARIA, avaria.getEstado());
        values.put(TIPO_AVARIA, avaria.getTipo());
        values.put(GRAVIDADE_AVARIA, avaria.getGravidade());
        values.put(DISPOSITIVO_AVARIA, avaria.getIdDispositivo());
        values.put(DATA_AVARIA, String.valueOf(avaria.getDate()));

        long id = this.sqLiteDatabase.insert(TABLE_NAME, null, values);

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
        values.put(DATA_AVARIA, String.valueOf(avaria.getDate()));

        return this.sqLiteDatabase.update(TABLE_NAME, values, "id = ?", new String[]{"" + avaria.getIdAvaria()}) > 0;
    }

    public boolean removerAvariaDB(int idAvaria){
        return (this.sqLiteDatabase.delete(TABLE_NAME, "id = ?", new String[]{"" + idAvaria}) == 1);
    }

    public void removerAllAvariasDB(){
        this.sqLiteDatabase.delete(TABLE_NAME, null, null);
    }

    public ArrayList<Avaria> getAllAvariasDB(){
        ArrayList<Avaria> avarias = new ArrayList<>();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME, new String[]{
                        ID_AVARIA, ESTADO_AVARIA, TIPO_AVARIA, GRAVIDADE_AVARIA, DISPOSITIVO_AVARIA, DESCRICAO_AVARIA, DATA_AVARIA},
                ESTADO_AVARIA + " IN (3,2,1,0)", null, null, null, ESTADO_AVARIA);

        if(cursor.moveToFirst()){
            do{
                Avaria auxAvaria = new Avaria(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3),
                        cursor.getInt(2), cursor.getInt(4), cursor.getString(6), cursor.getString(5));

                avarias.add(auxAvaria);
            }while(cursor.moveToNext());
        }

        return avarias;
    }

}
