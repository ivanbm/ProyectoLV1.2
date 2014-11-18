package com.izv.android.proyectolv12;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ivan on 16/11/2014.
 */
public class ClaseXML {

    public static void nuevoArchivo(Context contexto, ArrayList<Disco> lista){
        try{
            FileOutputStream fosxml = new FileOutputStream(new File(contexto.getFilesDir(),"archivo.xml"));

            XmlSerializer docxml= Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            docxml.startTag(null, "discografia");
            for(int i = 0; i<lista.size();i++){
                //System.out.println("ESCRIBIENDO "+lista.get(i).getAlbum());
                docxml.startTag(null, "disco");
                docxml.attribute(null, "album", lista.get(i).getAlbum());
                docxml.attribute(null, "autor", lista.get(i).getAutor());
                docxml.attribute(null, "discografica", lista.get(i).getDiscografica());
                docxml.attribute(null, "caratula", lista.get(i).getImagen());
                docxml.endTag(null, "disco");
            }
            docxml.endTag(null, "discografia");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        }catch(Exception e){
            System.out.println("ERROR AL ESCRIBIR");
        }
    }

    public static void eliminar(Context con, ArrayList<Disco> lista , Disco d){
        for(int i=0;i<lista.size();i++){
            if(lista.get(i).equals(d)){
                lista.remove(i);
                nuevoArchivo(con, lista);
                Collections.sort(lista);
                break;
            }
        }

    }

    public static void modificar(Context con, ArrayList<Disco> lista, Disco nuevoDisco, Disco antiguoDisco){
        for(int i=0;i<lista.size();i++){
            if(lista.get(i).equals(antiguoDisco)){
                lista.remove(i);
                lista.add(nuevoDisco);
                nuevoArchivo(con, lista);
                Collections.sort(lista);
                break;
            }
        }
    }

    public static ArrayList<Disco> leer(Context contexto){
        ArrayList<Disco> discos = new ArrayList<Disco>();

        try{
            XmlPullParser lectorxml= Xml.newPullParser();
            lectorxml.setInput(new FileInputStream(new File(contexto.getFilesDir(),"archivo.xml")),"utf-8");
            int evento = lectorxml.getEventType();

            while(evento!=XmlPullParser.END_DOCUMENT){
                if(evento==XmlPullParser.START_TAG){
                    String etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("disco")==0){
                        System.out.println("LEYENDO "+lectorxml.getAttributeValue(null, "album"));
                        discos.add(new Disco(lectorxml.getAttributeValue(null, "album"),
                                lectorxml.getAttributeValue(null, "autor"),
                                lectorxml.getAttributeValue(null, "discografica"),
                                lectorxml.getAttributeValue(null, "caratula")
                        ));
                    }
                }
                evento = lectorxml.next();
            }

        }catch (Exception e) {
            System.out.println("ERROR AL LEER");
        }

        if(discos.size()!=0){
            return discos;
        }else{
            return new ArrayList<Disco>();
        }

    }

}
