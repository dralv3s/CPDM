package com.drdev.cpdm2s2020.Service;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.drdev.cpdm2s2020.Model.TarefaModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TBL_TAREFAS = "TBL_TAREFAS";
    public static final String COLUMN_IDTAREFA = "IdTarefa";
    public static final String COLUMN_TITULO_TAREFA = "TituloTarefa";
    public static final String COLUMN_DESCRICAO_TAREFA = "DescricaoTarefa";
    public static final String COLUMN_ALIAS_TAREFA= "AliasTarefa";
    public static final String COLUMN_DATA_ENTREGA = "DataEntrega";
    public static final String COLUMN_NOTIFICAR = "Notificar";
    public static final String COLUMN_VALOR_NOTA = "ValorNota";
    public static final String COLUMN_ICONE_TAREFA = "IconeTarefa";
    public static final String COLUMN_FL_STATUS = "FlStatus";

    private StringBuilder sb;

    public DataBaseHelper(Context context) {
        super(context, "TaskLogger.db", null, 1);
        sb = new StringBuilder();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        sb.append("CREATE TABLE " + TBL_TAREFAS + " (");
        sb.append(COLUMN_IDTAREFA + " INTEGER NOT NULL, ");
        sb.append(COLUMN_TITULO_TAREFA + " TEXT NOT NULL, ");
        sb.append(COLUMN_DESCRICAO_TAREFA + " TEXT NOT NULL, ");
        sb.append(COLUMN_ALIAS_TAREFA + " TEXT, ");
        sb.append(COLUMN_DATA_ENTREGA + " TEXT NOT NULL, ");
        sb.append(COLUMN_NOTIFICAR + "  TEXT, ");
        sb.append(COLUMN_VALOR_NOTA + " NUMERIC, ");
        sb.append(COLUMN_ICONE_TAREFA + " INTEGER, ");
        sb.append(COLUMN_FL_STATUS + " INTEGER, ");
        sb.append("PRIMARY KEY(" + COLUMN_IDTAREFA + " AUTOINCREMENT) ");
        sb.append(");");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean SalvarTarefa(TarefaModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITULO_TAREFA, model.TituloTarefa);
        cv.put(COLUMN_DESCRICAO_TAREFA, model.DescricaoTarefa);
        cv.put(COLUMN_ALIAS_TAREFA, model.AliasTarefa);
        cv.put(COLUMN_DATA_ENTREGA, model.DataEntrega.toString());
        cv.put(COLUMN_NOTIFICAR, model.Notificar);
        cv.put(COLUMN_VALOR_NOTA, model.ValorNota);
        cv.put(COLUMN_ICONE_TAREFA, model.IconeTarefa);
        cv.put(COLUMN_FL_STATUS, model.FlStatus);

        long retorno = db.insert(TBL_TAREFAS, null, cv);

        db.close();

        return retorno != -1;
    }

    public ArrayList<TarefaModel> GetTarefasList() {

        ArrayList<TarefaModel> returnList = new ArrayList<TarefaModel>();

        TarefaModel model;

        String query = "SELECT * FROM " + TBL_TAREFAS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){

            do {
                model = new TarefaModel();

                model.IdTarefa = cursor.getInt(0);
                model.TituloTarefa = cursor.getString (1);
                model.DescricaoTarefa = cursor.getString(2);
                model.AliasTarefa = cursor.getString(3);

                try {
                    Date dateParse = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(4));
                    model.DataEntrega = dateParse;
                } catch (ParseException e) {
                    model.DataEntrega = null;
                }

                model.Notificar = cursor.getString(5);
                model.ValorNota = cursor.getInt(6);
                model.IconeTarefa = cursor.getInt(7);
                model.FlStatus = cursor.getInt(8);

                returnList.add(model);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return returnList;
    }
}