package com.drdev.cpdm2s2020.Model;

import java.util.Date;

public class TarefaModel {

    public Integer IdTarefa;

    public String TituloTarefa;

    public String AliasTarefa;

    public String DescricaoTarefa;

    public String Notificar;

    public Double ValorNota;

    public String DataEntrega;

    public Integer IconeTarefa;

    public Integer FlStatus;

    public TarefaModel() {
        FlStatus = 1;
    }
}
