package amsi.dei.estg.ipleiria.am.models;

import java.sql.Date;

public class Avaria {
    private int idAvaria, estado, gravidade, idDispositivo, tipo, idUtilizador;
    private String date;
    private String descricao;

    public int getIdAvaria() {
        return idAvaria;
    }

    public void setIdAvaria(int idAvaria) {
        this.idAvaria = idAvaria;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getGravidade() {
        return gravidade;
    }

    public void setGravidade(int gravidade) {
        this.gravidade = gravidade;
    }

    public int getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(int idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public Avaria(int id, int estado, int gravidade, int tipo, int idDispositivo, String date, String descricao, int idUtilizador) {
        this.idAvaria = id;
        this.estado = estado;
        this.gravidade = gravidade;
        this.idDispositivo = idDispositivo;
        this.date = date;
        this.descricao = descricao;
        this.tipo = tipo;
        this.idUtilizador = idUtilizador;
    }
}