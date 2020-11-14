package com.drdev.cpdm2s2020.Service;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.drdev.cpdm2s2020.Model.NotificacaoModel;
import com.drdev.cpdm2s2020.Model.TarefaModel;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TBL_TAREFAS = "TBL_TAREFAS";
    public static final String COLUMN_ID_TAREFA = "IdTarefa";
    public static final String COLUMN_TITULO_TAREFA = "TituloTarefa";
    public static final String COLUMN_DESCRICAO_TAREFA = "DescricaoTarefa";
    public static final String COLUMN_ALIAS_TAREFA= "AliasTarefa";
    public static final String COLUMN_DATA_ENTREGA = "DataEntrega";
    public static final String COLUMN_NOTIFICAR = "Notificar";
    public static final String COLUMN_VALOR_NOTA = "ValorNota";
    public static final String COLUMN_ICONE_TAREFA = "IconeTarefa";
    public static final String COLUMN_FL_STATUS = "FlStatus";
    public static final String TBL_NOTIFICACOES = "TBL_NOTIFICACOES";
    public static final String COLUMN_ID_NOTIFICACAO = "IdNotificacao";
    public static final String COLUMN_INICIO_NOTIFICACAO = "InicioNotificacao";
    public static final String COLUMN_FIM_NOTIFICACAO = "FimNotificacao";


    private StringBuilder sb;

    public DataBaseHelper(Context context) {
        super(context, "TaskLogger.db", null, 1);
        sb = new StringBuilder();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        sb.append("CREATE TABLE " + TBL_TAREFAS + " (");
        sb.append(COLUMN_ID_TAREFA + " INTEGER NOT NULL, ");
        sb.append(COLUMN_TITULO_TAREFA + " TEXT NOT NULL, ");
        sb.append(COLUMN_DESCRICAO_TAREFA + " TEXT NOT NULL, ");
        sb.append(COLUMN_ALIAS_TAREFA + " TEXT, ");
        sb.append(COLUMN_DATA_ENTREGA + " TEXT NOT NULL, ");
        sb.append(COLUMN_NOTIFICAR + "  TEXT, ");
        sb.append(COLUMN_VALOR_NOTA + " NUMERIC, ");
        sb.append(COLUMN_ICONE_TAREFA + " INTEGER, ");
        sb.append(COLUMN_FL_STATUS + " INTEGER, ");
        sb.append("PRIMARY KEY(" + COLUMN_ID_TAREFA + " AUTOINCREMENT) ");
        sb.append(");");

        db.execSQL(sb.toString());
        sb.delete(0, sb.length());

        sb.append("CREATE TABLE " + TBL_NOTIFICACOES + " (");
        sb.append(COLUMN_ID_NOTIFICACAO + " INTEGER NOT NULL, ");
        sb.append(COLUMN_ID_TAREFA + " INTEGER NOT NULL, ");
        sb.append(COLUMN_INICIO_NOTIFICACAO + " TEXT NOT NULL, ");
        sb.append(COLUMN_FIM_NOTIFICACAO + " TEXT NOT NULL, ");
        sb.append(COLUMN_FL_STATUS + " INTEGER, ");
        sb.append("PRIMARY KEY(" + COLUMN_ID_NOTIFICACAO + " AUTOINCREMENT) ");
        sb.append(");");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean SalvarNotificacao (NotificacaoModel model){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID_TAREFA, model.IdTarefa);
        cv.put(COLUMN_INICIO_NOTIFICACAO, model.InicioNotificacaoStr);
        cv.put(COLUMN_FIM_NOTIFICACAO, model.FimNotificacaoStr);
        cv.put(COLUMN_FL_STATUS, model.FlStatus);
        long retorno = db.insert(TBL_NOTIFICACOES, null, cv);
        db.close();
        return retorno != -1;
    }

    public ArrayList<NotificacaoModel> GetNotificacoes() {
        ArrayList<NotificacaoModel> returnList = new ArrayList<NotificacaoModel>();
        NotificacaoModel model;
        String query = "SELECT N." + COLUMN_ID_NOTIFICACAO +
                            ", N." + COLUMN_ID_TAREFA +
                            ", T." + COLUMN_ALIAS_TAREFA +
                            ", T." + COLUMN_DESCRICAO_TAREFA +
                            ", T." + COLUMN_TITULO_TAREFA +
                            ", N." + COLUMN_INICIO_NOTIFICACAO +
                            ", N." + COLUMN_FIM_NOTIFICACAO +
                            ", N." + COLUMN_FL_STATUS +
                            " FROM " + TBL_NOTIFICACOES + " N" +
                            " JOIN " + TBL_TAREFAS + " T ON N." + COLUMN_ID_TAREFA + " = T." + COLUMN_ID_TAREFA +
                            " WHERE " + COLUMN_FL_STATUS + " = 1 ;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                model = new NotificacaoModel();
                model.IdNotificacao = cursor.getInt(0);
                model.IdTarefa = cursor.getInt (1);
                model.NomeTarefa = cursor.getString(2);
                model.DescricaoNotificacao = cursor.getString(3);
                model.TituloNotificacao = cursor.getString(4);
                model.InicioNotificacaoStr = cursor.getString(5);
                model.FimNotificacaoStr = cursor.getString(6);
                model.FlStatus = cursor.getInt(7);
                returnList.add(model);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public boolean SalvarTarefa(TarefaModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITULO_TAREFA, model.TituloTarefa);
        cv.put(COLUMN_DESCRICAO_TAREFA, model.DescricaoTarefa);
        cv.put(COLUMN_ALIAS_TAREFA, model.AliasTarefa);
        cv.put(COLUMN_DATA_ENTREGA, model.DataEntrega);
        cv.put(COLUMN_NOTIFICAR, model.Notificar);
        cv.put(COLUMN_VALOR_NOTA, model.ValorNota);
        cv.put(COLUMN_ICONE_TAREFA, model.IconeTarefa);
        cv.put(COLUMN_FL_STATUS, model.FlStatus);
        long retorno = db.insert(TBL_TAREFAS, null, cv);
        db.close();
        return retorno != -1;
    }

    public ArrayList<TarefaModel> GetTarefas() {
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
                model.DataEntrega = cursor.getString(4);
                model.Notificar = cursor.getString(5);
                model.ValorNota = cursor.getDouble(6);
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

    public TarefaModel GetTarefa(int IdTarefa) {
        TarefaModel model = new TarefaModel();
        String query = "SELECT * FROM " + TBL_TAREFAS + " WHERE " + COLUMN_ID_TAREFA + " = " + IdTarefa;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            model.IdTarefa = cursor.getInt(0);
            model.TituloTarefa = cursor.getString (1);
            model.DescricaoTarefa = cursor.getString(2);
            model.AliasTarefa = cursor.getString(3);
            model.DataEntrega = cursor.getString(4);
            model.Notificar = cursor.getString(5);
            model.ValorNota = cursor.getDouble(6);
            model.IconeTarefa = cursor.getInt(7);
            model.FlStatus = cursor.getInt(8);
        }
        cursor.close();
        db.close();
        return model;
    }

    public boolean UpdateTarefa(TarefaModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITULO_TAREFA, model.TituloTarefa);
        cv.put(COLUMN_DESCRICAO_TAREFA, model.DescricaoTarefa);
        cv.put(COLUMN_ALIAS_TAREFA, model.AliasTarefa);
        cv.put(COLUMN_DATA_ENTREGA, model.DataEntrega);
        cv.put(COLUMN_NOTIFICAR, model.Notificar);
        cv.put(COLUMN_VALOR_NOTA, model.ValorNota);
        cv.put(COLUMN_ICONE_TAREFA, model.IconeTarefa);
        long retorno = db.update(TBL_TAREFAS, cv, "IdTarefa = " + model.IdTarefa, null);
        db.close();
        return retorno != -1;
    }

    public boolean ExcluirTarefa(TarefaModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FL_STATUS, 1);
        long retorno = db.update(TBL_TAREFAS, cv, "IdTarefa = " + model.IdTarefa, null);
        db.close();
        return retorno != -1;
    }
}