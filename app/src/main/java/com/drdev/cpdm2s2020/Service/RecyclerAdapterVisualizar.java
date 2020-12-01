package com.drdev.cpdm2s2020.Service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.drdev.cpdm2s2020.Activity.CriarActivity;
import com.drdev.cpdm2s2020.Activity.ExibirActivity;
import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RecyclerAdapterVisualizar extends RecyclerView.Adapter<RecyclerAdapterVisualizar.TarefaViewHolder> {

    Context context;
    ArrayList<TarefaModel> tarefasList;
    int action;
    DataBaseHelper dbHelper;
    int currentPosition = 0;

    public RecyclerAdapterVisualizar (Context _context, ArrayList<TarefaModel> _tarefasList, int _action, DataBaseHelper _dbHelper)
    {
        tarefasList = _tarefasList;
        context = _context;
        action = _action;
        dbHelper = _dbHelper;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.visu_row, parent, false);

        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, final int position) {

        String complemento = "";

        String title = tarefasList.get(position).TituloTarefa;
        String alias = tarefasList.get(position).AliasTarefa;

        complemento = alias.length() != 0 ? " (" + alias + ")" : "";

        holder.tarefaTitle.setText(title + complemento);

        String descricao = tarefasList.get(position).DescricaoTarefa;

        if (descricao.length() > 36)
            descricao = descricao.substring(0, 36);

        complemento = descricao.length() >= 36 ? "...":"";

        holder.tarefaDesc.setText(descricao + complemento) ;

        holder.tarefaImage.setImageResource(tarefasList.get(position).IconeTarefa);

        holder.cardViewItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               //
                onTarefaLongClick(position);
                return true;
            }
        });

        holder.cardViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTarefaClick(position);
            }
        });
    }

    private void onTarefaClick(int position) {
        TarefaModel model = tarefasList.get(position);
        Intent intent = new Intent(context, CriarActivity.class);
        intent.putExtra(context.getString(R.string.IdTarefa), model.IdTarefa);
        intent.putExtra(context.getString(R.string.Action), action);
        context.startActivity(intent);
    }

    private void onTarefaLongClick(int position) {

        currentPosition = position;

        Context contextMaterialTheme = context;
        contextMaterialTheme.setTheme(R.style.MaterialTheme);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(contextMaterialTheme);
        builder.setTitle("Deseja apagar a tarefa?");

        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteRecord();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void DeleteRecord(){
        dbHelper.ExcluirTarefa(tarefasList.get(currentPosition));
        tarefasList.remove(currentPosition);
        this.notifyItemRemoved(currentPosition);
        this.notifyItemRangeChanged(currentPosition, tarefasList.size());
       Toast.makeText(context, R.string.labelDeleteSnackBar, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return tarefasList.size();
    }

    public class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tarefaTitle, tarefaDesc;
        ImageView tarefaImage;
        CardView cardViewItem;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tarefaImage = itemView.findViewById(R.id.visRowImageView);
            tarefaTitle = itemView.findViewById(R.id.visRowTitle);
            tarefaDesc = itemView.findViewById(R.id.visRowDesc);
            cardViewItem = itemView.findViewById(R.id.cardViewItem);
        }
    }
}
