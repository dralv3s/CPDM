package com.drdev.cpdm2s2020.Service;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.drdev.cpdm2s2020.Model.TarefaModel;
import com.drdev.cpdm2s2020.R;

import java.util.ArrayList;

public class RecyclerAdapterVisualizar extends RecyclerView.Adapter<RecyclerAdapterVisualizar.TarefaViewHolder> {

    Context context;
    ArrayList<TarefaModel> tarefasList;

    public RecyclerAdapterVisualizar (Context _context, ArrayList<TarefaModel> _tarefasList){
        tarefasList = _tarefasList;
        context = _context;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.visu_row, parent, false);

        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {

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
    }

    @Override
    public int getItemCount() {
        return tarefasList.size();
    }

    public class TarefaViewHolder extends RecyclerView.ViewHolder{

        TextView tarefaTitle, tarefaDesc;
        ImageView tarefaImage;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tarefaImage = itemView.findViewById(R.id.visRowImageView);
            tarefaTitle = itemView.findViewById(R.id.visRowTitle);
            tarefaDesc = itemView.findViewById(R.id.visRowDesc);
        }
    }
}
