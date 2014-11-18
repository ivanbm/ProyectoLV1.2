package com.izv.android.proyectolv12;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivan on 30/09/2014.
 */
public class Disco implements Comparable<Disco>, Parcelable{

    private String album, autor, discografica, imagen;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(album);
        parcel.writeString(autor);
        parcel.writeString(discografica);
        parcel.writeString(imagen);
    }

    public Disco(Parcel p){
        this(p.readString(), p.readString(), p.readString(), p.readString());
    }

    public static final Parcelable.Creator<Disco> CREATOR = new Parcelable.Creator<Disco>() {
        @Override
        public Disco createFromParcel(Parcel parcel) {
            return new Disco(parcel);
        }
        @Override
        public Disco[] newArray(int size) {
            return new Disco[size];
        }
    };


    public Disco(String album, String autor, String discografica, String imagen) {
        this.album = album;
        this.autor = autor;
        this.discografica = discografica;
        this.imagen = imagen;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDiscografica() {
        return discografica;
    }

    public void setDiscografica(String discografica) {
        this.discografica = discografica;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public int compareTo(Disco disco) {
        return this.getAlbum().compareTo(disco.getAlbum());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Disco disco = (Disco) o;

        if (!album.equals(disco.album)) return false;
        if (!autor.equals(disco.autor)) return false;
        if (!discografica.equals(disco.discografica)) return false;
        if (imagen != null ? !imagen.equals(disco.imagen) : disco.imagen != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = album.hashCode();
        result = 31 * result + autor.hashCode();
        result = 31 * result + discografica.hashCode();
        result = 31 * result + (imagen != null ? imagen.hashCode() : 0);
        return result;
    }
}

