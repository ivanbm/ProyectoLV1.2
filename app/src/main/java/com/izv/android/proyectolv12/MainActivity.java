package com.izv.android.proyectolv12;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends Activity {

    private ArrayList<Disco> datos;
    private Adaptador ad;
    private String caratula;
    private Bitmap caratuladefault;
    private ImageView ivCover;
    private boolean coverSeleccionada;
    private final int SELECT_IMAGE = 0;
    private final int ANADIR_DISCO = 1;


    /*-------------------------------------------*/
    /*              METODOS ON                   */
    /*-------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        initComponents();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            anadir();
        }else if (id == R.id.orderbyAlbum) {
            ordenarAlbum();
        }else if (id == R.id.orderbyAutor) {
            ordenarAutor();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.editar:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                //1. obtenemos el indice
                int index = info.position;
                editar(index);
                ad.notifyDataSetChanged();

                return true;
            case R.id.elimiar:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index2 = info.position;
                eliminar(index2);
                ad.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }

        /*-------------------------------------------*/
        /*          METODOS CAMBIO ORIENTACION       */
        /*-------------------------------------------*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("discos", datos);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        datos = (ArrayList<Disco>)savedInstanceState.getSerializable("discos");
        mostrarDiscos();
    }




        /*-------------------------------------------*/
        /*              METODOS PROPIOS              */
        /*-------------------------------------------*/

    private void initComponents(){
        datos = new ArrayList<Disco>();
        Bitmap def = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.nocover);
        caratuladefault = Bitmap.createScaledBitmap(def, 200, 200, false);
        coverSeleccionada = false;
        /*Disco dis1 = new Disco("Ghost Stories", "Coldplay", "Sony Music", caratula);
        Disco dis2 = new Disco("Memories", "David Guetta", "Parlophone", caratula);
        Disco dis3 = new Disco("V", "Maroon 5", "Warner Music", caratula);
        Disco dis4 = new Disco("Demons", "Imagine Dragons", "Virgin Music", caratula);
        Disco dis5 = new Disco("Songs Of Innocence", "U2", "Warner Music", caratula);
        datos.add(dis1);
        datos.add(dis2);
        datos.add(dis3);
        datos.add(dis4);
        datos.add(dis5);*/

        ClaseXML cxml = new ClaseXML();
        datos = cxml.leer(getApplicationContext());

        mostrarDiscos();

    }

    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    /*-------------------------------------*/
    /*--       VISTUALIZAR DISCOS        --*/
    /*-------------------------------------*/

    public void mostrarDiscos(){
        ad = new Adaptador(this, R.layout.lista_detalle, datos);
        final ListView ls = (ListView)findViewById(R.id.lvLista);
        ls.setAdapter(ad);
        registerForContextMenu(ls);
    }

    /*-------------------------------------*/
    /*--           AÑADIR DISCO          --*/
    /*-------------------------------------*/
    public void anadir(){
        Intent i = new Intent(this,Anadir.class);
        startActivityForResult(i, ANADIR_DISCO);
    }


    /*-------------------------------------*/
    /*--           EDITAR DISCO          --*/
    /*-------------------------------------*/


    public boolean editar(final int index){
        final String aut = datos.get(index).getAutor();
        final String alb = datos.get(index).getAlbum();
        final String dis = datos.get(index).getDiscografica();
        final String caratulaold = datos.get(index).getImagen();

        final AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle(R.string.tituloEditar);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.anadir, null);
        alert.setView(vista);

        final EditText et1,et2, discografica;
        et1 = (EditText) vista.findViewById(R.id.etAlbum);
        et2 = (EditText) vista.findViewById(R.id.etAutor);
        discografica = (EditText) vista.findViewById(R.id.etDiscografica);
        //et3 = (EditText) vista.findViewById(R.id.discografica);
        ivCover = (ImageView) vista.findViewById(R.id.ivCover);

        et1.setText(alb);
        et2.setText(aut);
        discografica.setText(dis);

        if(!datos.get(index).getImagen().equals("vacio")) {
            Bitmap bitmap = BitmapFactory.decodeFile(datos.get(index).getImagen());
            Bitmap img = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
            ivCover.setImageBitmap(img);
        }else{
            ivCover.setImageResource(R.drawable.ic_launcher);
        }
        caratula = datos.get(index).getImagen();

        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFoto(view);
            }
        });

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Disco antiguoDisco = new Disco(alb, aut, dis, caratulaold);
                Disco nuevoDisco = new Disco(et1.getText().toString(),et2.getText().toString(), discografica.getText().toString(),caratula);
                //datos.set(index, nuevoDisco);
                Collections.sort(datos);

                ClaseXML cxml = new ClaseXML();
                cxml.modificar(getApplicationContext(), datos, nuevoDisco, antiguoDisco);

                ad.notifyDataSetChanged();
                tostada(getString(R.string.msgeditar));
                mostrarDiscos();
            }
        });
        alert.setNegativeButton(android.R.string.no ,null);
        alert.show();

        return true;
    }


    /*-------------------------------------*/
    /*--        ELIMINAR0 DISCO           --*/
    /*-------------------------------------*/

    public void eliminar(int index){
        String aut = datos.get(index).getAutor();
        String alb = datos.get(index).getAlbum();
        String dis = datos.get(index).getDiscografica();
        String caratulaold = datos.get(index).getImagen();
        Disco d = new Disco(alb,aut,dis,caratulaold);

        ClaseXML cxml = new ClaseXML();
        cxml.eliminar(getApplicationContext(), datos, d);

        Collections.sort(datos);
        tostada(getString(R.string.msgeliminar));
        mostrarDiscos();
    }


    /*----------------------------------------------------*/
    /*                  SELECCIONAR IMAGENES              */
    /*----------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_IMAGE:
                    Uri selectedImageUri = data.getData();
                    caratula = getPath(getApplicationContext(), selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(caratula);
                    ivCover.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
                    coverSeleccionada = true;
                    break;

                case ANADIR_DISCO:
                    String alb, aut, dis, car;
                    Bundle dsc = data.getExtras();
                    aut = dsc.getString("autor");
                    alb = dsc.getString("album");
                    dis = dsc.getString("discografica");
                    car = dsc.getString("cover");
                    if(car == null){
                        car = "vacio";
                    }

                    datos.add(new Disco(aut,alb,dis,car));

                    ClaseXML cxml = new ClaseXML();
                    cxml.nuevoArchivo(getApplicationContext(),datos);
                    datos = cxml.leer(getApplicationContext());

                    mostrarDiscos();
                    ad.notifyDataSetChanged();
                    tostada(getString(R.string.msganadir));
                    break;
            }
        }else{
            coverSeleccionada = false;
        }
    }


    public void selectFoto(View v){
        Intent foto = new Intent(Intent.ACTION_PICK);
        foto.setType("image/*");
        startActivityForResult(foto, SELECT_IMAGE);
    }

    private String getPath(Context context, Uri uri){
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /*----------------------------------------------------*/
    /*                  METODOS ORDENACION                */
    /*----------------------------------------------------*/

    public void ordenarAlbum(){
        Collections.sort(datos, new Comparator<Disco>() {
            @Override
            public int compare(Disco d1, Disco d2) {
                return d1.getAlbum().compareTo(d2.getAlbum());
            }
        });
        ad.notifyDataSetChanged();
    }

    public void ordenarAutor(){
        Collections.sort(datos, new Comparator<Disco>() {
            @Override
            public int compare(Disco d1, Disco d2) {
                return d1.getAutor().compareTo(d2.getAutor());
            }
        });
        ad.notifyDataSetChanged();
    }

}