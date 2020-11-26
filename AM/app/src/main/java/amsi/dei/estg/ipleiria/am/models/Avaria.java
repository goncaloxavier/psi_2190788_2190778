package amsi.dei.estg.ipleiria.am.models;

public class Avaria {
    private int idAvaria, estado, gravidade, idDispositivo, date;
    private String descricao, referencia;

    private static int autoIncrementId = 1;

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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Avaria(int idAvaria, int estado, int gravidade, int idDispositivo, int date, String descricao) {
        this.idAvaria = autoIncrementId++;
        this.estado = estado;
        this.gravidade = gravidade;
        this.idDispositivo = idDispositivo;
        this.date = date;
        this.descricao = descricao;
    }
}