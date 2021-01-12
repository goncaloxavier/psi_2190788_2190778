package amsi.dei.estg.ipleiria.am.models;

public class Dispositivo {
    private int idDispositivo, estado;
    private String dataCompra, tipo, referencia;

    public int getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(int idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Dispositivo(int idDispositivo, int estado, String dataCompra, String tipo, String referencia) {
        this.idDispositivo = idDispositivo;
        this.estado = estado;
        this.dataCompra = dataCompra;
        this.tipo = tipo;
        this.referencia = referencia;
    }
}