package amsi.dei.estg.ipleiria.am.models;

public class Utilizador {
    private String nomeUtilizador, palavraPasse, email;
    private int tipo, estado, idUtilizador;

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getNomeUtilizador() {
        return nomeUtilizador;
    }

    public void setNomeUtilizador(String nomeUtilizador) {
        this.nomeUtilizador = nomeUtilizador;
    }

    public String getPalavraPasse() {
        return palavraPasse;
    }

    public void setPalavraPasse(String palavraPasse) {
        this.palavraPasse = palavraPasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Utilizador(String nomeUtilizador, String palavraPasse, String email, int tipo, int estado, int idUtilizador) {
        this.nomeUtilizador = nomeUtilizador;
        this.palavraPasse = palavraPasse;
        this.email = email;
        this.tipo = tipo;
        this.estado = estado;
        this.idUtilizador = idUtilizador;
    }

    public Utilizador(int idUtilizador ,String nomeUtilizador){
        this.idUtilizador = idUtilizador;
        this.nomeUtilizador = nomeUtilizador;
    }
}
