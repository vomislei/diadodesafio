package br.edu.utfpr.diadodesafio;

import java.util.Date;

public class Usuario {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String email;
    private String latitude;
    private String longitude;
    private Double media;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Usuario(String id, String email, String latitude, String longitude, Double media,String data) {
        this.id = id;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.media = media;
        this.data = data;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
