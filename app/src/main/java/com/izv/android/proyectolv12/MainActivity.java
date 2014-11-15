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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends Activity {

    private ArrayList<Disco> datos;
    private Adaptador ad;
    private Bitmap caratula, caratuladefault;
    private ImageView ivCover;
    private boolean coverSeleccionada;
    private final int SELECT_IMAGE = 1;


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
                datos.remove(info.position);
                Collections.sort(datos);
                ad.notifyDataSetChanged();
                tostada(getString(R.string.msgeliminar));
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
        Disco dis1 = new Disco("Ghost Stories", "Coldplay", "Sony Music", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.coldplay));
        Disco dis2 = new Disco("Memories", "David Guetta", "Parlophone",BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.davidguetta));
        Disco dis3 = new Disco("V", "Maroon 5", "Warner Music",BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.maroon5));
        Disco dis4 = new Disco("Demons", "Imagine Dragons", "Virgin Music",BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.imaginedragons));
        Disco dis5 = new Disco("Songs Of Innocence", "U2", "Warner Music",BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.u2));
        datos.add(dis1);
        datos.add(dis2);
        datos.add(dis3);
        datos.add(dis4);
        datos.add(dis5);


        mostrarDiscos();

    }

    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    // METODOS DE VISUALIZACION

    public void mostrarDiscos(){
        ad = new Adaptador(this, R.layout.lista_detalle, datos);
        final ListView ls = (ListView)findViewById(R.id.lvLista);
        ls.setAdapter(ad);
        registerForContextMenu(ls);
    }


    public boolean anadir(){
        final AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle(R.string.tituloAnadir);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.anadir, null);
        alert.setView(vista);

        ivCover = (ImageView)vista.findViewById(R.id.ivCover);
        ivCover.setImageBitmap(caratuladefault);

        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFoto(view);
            }
        });

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText et1,et2, discografica;
                et1 = (EditText) vista.findViewById(R.id.etAlbum);
                et2 = (EditText) vista.findViewById(R.id.etAutor);
                discografica = (EditText) vista.findViewById(R.id.etDiscografica);
                ivCover = (ImageView)vista.findViewById(R.id.ivCover);
                ivCover.setImageBitmap(caratuladefault);

                if(coverSeleccionada) {
                    datos.add(new Disco(et1.getText().toString(), et2.getText().toString(), discografica.getText().toString(), caratula));
                }else{
                    datos.add(new Disco(et1.getText().toString(), et2.getText().toString(), discografica.getText().toString(), caratuladefault));
                }
                coverSeleccionada = false;
                Collections.sort(datos);
                ad.notifyDataSetChanged();
                tostada(getString(R.string.msganadir));
            }
        });
        alert.setNegativeButton(android.R.string.no ,null);
        alert.show();

        return true;
    }


    /*-------------------------------------*/
    /*--           EDITAR DISCO          --*/
    /*-------------------------------------*/


    public boolean editar(final int index){
        String aut = datos.get(index).getAutor();
        String alb = datos.get(index).getAlbum();
        String dis = datos.get(index).getDiscografica();

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
        ivCover.setImageBitmap(datos.get(index).getImagen());
        caratula = datos.get(index).getImagen();

        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFoto(view);
            }
        });

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                datos.set(index, new Disco(et1.getText().toString(),et2.getText().toString(), discografica.getText().toString(),caratula));
                Collections.sort(datos);
                ad.notifyDataSetChanged();
                tostada(getString(R.string.msgeditar));
            }
        });
        alert.setNegativeButton(android.R.string.no ,null);
        alert.show();

        return true;
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
                    String path = getPath(getApplicationContext(), selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    caratula = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
                    ivCover.setImageBitmap(caratula);
                    coverSeleccionada = true;
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
        for (int i = 0; i < datos.size() ; i++) {
            System.out.println(datos.get(i).getAutor());
        }
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
