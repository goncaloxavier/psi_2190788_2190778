package amsi.dei.estg.ipleiria.am.models;

public class Estatistica {
    private int numAvarias, numAvariasR, numAvariasNR, numDispositivosF, numDispositivosNF;

    public int getNumAvarias() {
        return numAvarias;
    }

    public void setNumAvarias(int numAvarias) {
        this.numAvarias = numAvarias;
    }

    public int getNumAvariasR() {
        return numAvariasR;
    }

    public void setNumAvariasR(int numAvariasR) {
        this.numAvariasR = numAvariasR;
    }

    public int getNumAvariasNR() {
        return numAvariasNR;
    }

    public void setNumAvariasNR(int numAvariasNR) {
        this.numAvariasNR = numAvariasNR;
    }

    public int getNumDispositivosF() {
        return numDispositivosF;
    }

    public void setNumDispositivosF(int numDispositivosF) {
        this.numDispositivosF = numDispositivosF;
    }

    public int getNumDispositivosNF() {
        return numDispositivosNF;
    }

    public void setNumDispositivosNF(int numDispositivosNF) {
        this.numDispositivosNF = numDispositivosNF;
    }

    public Estatistica(int numAvarias, int numAvariasR, int numAvariasNR, int numDispositivosF, int numDispositivosNF) {
        this.numAvarias = numAvarias;
        this.numAvariasR = numAvariasR;
        this.numAvariasNR = numAvariasNR;
        this.numDispositivosF = numDispositivosF;
        this.numDispositivosNF = numDispositivosNF;
    }

    public Estatistica() {

    }
}
