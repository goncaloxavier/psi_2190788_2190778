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

public class ListaDispositivosAdaptor extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Dispositivo> dispositivos;

    public ListaDispositivosAdaptor(Context context, ArrayList<Dispositivo> dispositivos) {
        this.context = context;
        this.dispositivos = dispositivos;
    }

    @Override
    public int getCount() {
        int size = 0;
        if(dispositivos != null){
            size = dispositivos.size();
        }

        return size;
    }

    @Override
    public Object getItem(int position) {
        return dispositivos.get(position);
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
            convertView = layoutInflater.inflate(R.layout.item_lista_dispositivo, null);
        }

        ViewHolderLista viewHolderLista = (ViewHolderLista) convertView.getTag();
        if(viewHolderLista == null){
            viewHolderLista = new ViewHolderLista(convertView);
            convertView.setTag(viewHolderLista);
        }
        viewHolderLista.update(dispositivos.get(position));


        return convertView;
    }

    private class ViewHolderLista{
        private final TextView dataCompra, tipo, referencia, estado;

        public ViewHolderLista(View convertView){
            dataCompra = convertView.findViewById(R.id.tvDataCompra);
            tipo = convertView.findViewById(R.id.tvTipoD);
            referencia = convertView.findViewById(R.id.tvReferencia);
            estado = convertView.findViewById(R.id.tvEstadoD);
        }

        public void update(Dispositivo dispositivo){
            try{
                dataCompra.setText(dispositivo.getDataCompra());
                tipo.setText(dispositivo.getTipo());
                referencia.setText(dispositivo.getReferencia());
                estado.setText("");
                switch (dispositivo.getEstado()){
                    case 0:
                        estado.setBackgroundColor(Color.parseColor("#FFA500"));
                        break;
                    case 1:
                        estado.setBackgroundColor(Color.GREEN);
                        break;
                }
            }catch (NullPointerException e){
                Toast.makeText(context, "Erro ao carregar informação tente novamente.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
