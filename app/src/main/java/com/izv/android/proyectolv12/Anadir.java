package com.izv.android.proyectolv12;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Ivan on 15/11/2014.
 */
public class Anadir extends MainActivity{

    private String autor, album, discografica, caratula;
    private final int SELECT_IMAGE_ADD = 2;
    private boolean coverSeleccionada;
    private ImageView ivCover;
    private ArrayList<Disco> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intentanadir);
        initComponents();
        System.out.println("VISTA "+(ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_IMAGE_ADD:
                    Uri selectedImageUri = data.getData();
                    caratula = getPath(getApplicationContext(), selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(caratula);
                    ivCover.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
                    coverSeleccionada = true;
                    break;
            }
        }else{
            coverSeleccionada = false;
        }
    }

    public void btAnadir(View v){
        EditText aut, alb, disc;
        ImageView car;
        //System.out.println(caratula);
        aut = (EditText)findViewById(R.id.etAutor);
        alb = (EditText)findViewById(R.id.etAlbum);
        disc = (EditText)findViewById(R.id.etDiscografica);

        autor = aut.getText().toString();
        album = alb.getText().toString();
        discografica = disc.getText().toString();

        Intent result = new Intent();
        result.putExtra("autor", autor);
        result.putExtra("album", album);
        result.putExtra("discografica", discografica);
        result.putExtra("cover", caratula);
        setResult(Activity.RESULT_OK, result);
        finish();

        this.finish();
    }

    public void initComponents() {
        ivCover = (ImageView)findViewById(R.id.ivCover);
        ivCover.setImageResource(R.drawable.ic_launcher);

        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFoto(view);
            }
        });
    }

    public void selectFoto(View v){
        Intent foto = new Intent(Intent.ACTION_PICK);
        foto.setType("image/*");
        startActivityForResult(foto, SELECT_IMAGE_ADD);
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
}
