package com.example.victo.arcdc.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.victo.arcdc.R;

public class TabelaComandosAdapter extends ArrayAdapter<String> {
    int[] comandos;
    Context ctx;

    public TabelaComandosAdapter(@NonNull Context context, int [] cmd) {
        super(context, R.layout.gridview_item);

        this.comandos = cmd;
        this.ctx = context;
    }

    @Override
    public int getCount() {
        return comandos.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TabelaComandosAdapter.ViewHolder viewHolder = new TabelaComandosAdapter.ViewHolder();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
            viewHolder.Comandos = convertView.findViewById(R.id.list_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (TabelaComandosAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.Comandos.setImageResource(comandos[position]);
        return convertView;
    }

    static class ViewHolder{

        ImageView Comandos;
    }
}
