package amsi.dei.estg.ipleiria.am.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.am.R;
import amsi.dei.estg.ipleiria.am.models.Avaria;
import amsi.dei.estg.ipleiria.am.models.Dispositivo;
import amsi.dei.estg.ipleiria.am.models.SingletonGestorAvarias;
import amsi.dei.estg.ipleiria.am.models.Utilizador;

public class ListaAvariasAdaptor extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Avaria> avarias;
    private ArrayList<Dispositivo> dispositivos;
    private ArrayList<Utilizador> utilizadores;

    public ListaAvariasAdaptor(Context context, ArrayList<Avaria> avarias) {
        this.context = context;
        this.avarias = avarias;
    }

    @Override
    public int getCount() {
        int size = 0;
        if(avarias != null){
            size = avarias.size();
        }

        return size;
    }

    @Override
    public Object getItem(int position) {
        return avarias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_lista_avaria, null);
        }

        ViewHolderLista viewHolderLista = (ViewHolderLista) convertView.getTag();
        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(convertView);
            convertView.setTag(viewHolderLista);
        }
        viewHolderLista.update(avarias.get(position));


        return convertView;
    }

    private class ViewHolderLista{
        private final TextView descricao, dataAvaria, referencia, tipo, estado, utilizador;

        public ViewHolderLista(View convertView){
            descricao = convertView.findViewById(R.id.tvEstadoD);
            dataAvaria = convertView.findViewById(R.id.tvDataCompra);
            referencia = convertView.findViewById(R.id.tvReferencia);
            utilizador = convertView.findViewById(R.id.tvTipoD);
            tipo = convertView.findViewById(R.id.tvTipo);
            estado = convertView.findViewById(R.id.tvEstado);
        }

        public void update(Avaria avaria){
            try{
                descricao.setText(avaria.getDescricao());
                dataAvaria.setText(avaria.getDate());
                referencia.setText(SingletonGestorAvarias.getInstance(context).getDispositivo(avaria.getIdDispositivo()).getReferencia());
                utilizador.setText(SingletonGestorAvarias.getInstance(context).getUtilizador(avaria.getIdUtilizador()).getNomeUtilizador());
                estado.setText("");
                if(avaria.getTipo() == 0){
                    tipo.setText("Hardware");
                }else{
                    tipo.setText("Software");
                }
                switch (avaria.getEstado()){
                    case 0:
                        estado.setBackgroundColor(Color.parseColor("#FFA500"));
                        break;
                    case 1:
                        estado.setBackgroundColor(Color.YELLOW);
                        break;
                    case 2:
                        estado.setBackgroundColor(Color.GREEN);
                        break;
                }
            }catch (NullPointerException e){
                //Toast.makeText(context, "Erro ao carregar informação tente novamente.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
