package com.drdev.cpdm2s2020.Model;
import java.util.Date;

public class NotificacaoModel {
    public Integer IdNotificacao;
    public Integer IdTarefa;
    public String NomeTarefa;
    public String DescricaoNotificacao;
    public String TituloNotificacao;
    public Date InicioNotificacao;
    public Date FimNotificacao;
    public String InicioNotificacaoStr;
    public String FimNotificacaoStr;
    public Integer FlStatus;

    public NotificacaoModel(){
        FlStatus = 1;
    }
}