package com.izv.android.proyectolv12;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class Adaptador extends ArrayAdapter<Disco> {

    private ArrayList<Disco> lista;
    private Context contexto;
    private int recurso;
    private static LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Disco> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView tv1, tv2, tv3;
        public ImageView iv;
        public int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = i.inflate(recurso, null);

            ImageView iv1 = (ImageView)convertView.findViewById(R.id.ivImagen);
            vh = new ViewHolder();
            vh.tv1 = (TextView)convertView.findViewById(R.id.tvTexto1);
            vh.tv2 = (TextView)convertView.findViewById(R.id.tvTexto2);
            vh.tv3 = (TextView)convertView.findViewById(R.id.tvTexto3);
            vh.iv = (ImageView)convertView.findViewById(R.id.ivImagen);

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }

        vh.position = position;
        vh.tv1.setTag(position);
        //Log.v("LOG",vh.tv1.getTag().toString());
        vh.tv1.setText(lista.get(position).getAlbum());
        vh.tv2.setText(lista.get(position).getAutor());
        vh.tv3.setText(lista.get(position).getDiscografica());
        Bitmap caratula = Bitmap.createScaledBitmap(lista.get(position).getImagen(), 200, 200, false);
        caratula = getRoundedCornerBitmap(caratula);
        vh.iv.setImageBitmap(caratula);
        vh.iv.setTag(position);

        /*if(lista.get(position).getAutor()=="Coldplay"){
            img = R.drawable.coldplay;
        }else if (lista.get(position).getAutor()=="Imagine Dragons"){
            img = R.drawable.imaginedragons;
        }else if (lista.get(position).getAutor()=="David Guetta"){
            img = R.drawable.davidguetta;
        }else if (lista.get(position).getAutor()=="U2"){
            img = R.drawable.u2;
        }else if (lista.get(position).getAutor()=="Maroon 5"){
            img = R.drawable.maroon5;
        }else{
            img = R.drawable.nocover;
        }

        vh.iv1.setImageResource(img);*/
        return convertView;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 150;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
