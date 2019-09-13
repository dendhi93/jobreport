package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_image")
public class MasterImage implements Serializable {
    @DatabaseField(id = true)
    private Integer id_image;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "image_name")
    private String image_name;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;
    @DatabaseField(columnName = "image_url")
    private String image_url;
    @DatabaseField(columnName = "conn_type")
    private String conn_type;
    @DatabaseField(columnName = "un_user")
    private String un_user;
    @DatabaseField(columnName = "image_description")
    private String image_description;
    @DatabaseField(columnName = "t_image_lat")
    private String t_image_lat;
    @DatabaseField(columnName = "t_image_lng")
    private String t_image_lng;
    @DatabaseField(columnName = "t_image_address")
    private String t_image_address;

    public Integer getId_image() {
        return id_image;
    }

    public void setId_image(Integer id_image) {
        this.id_image = id_image;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getProgress_type() {
        return progress_type;
    }

    public void setProgress_type(String progress_type) {
        this.progress_type = progress_type;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getConn_type() {
        return conn_type;
    }

    public void setConn_type(String conn_type) {
        this.conn_type = conn_type;
    }

    public String getUn_user() {
        return un_user;
    }

    public void setUn_user(String un_user) {
        this.un_user = un_user;
    }


    public String getImage_description() {
        return image_description;
    }

    public void setImage_description(String image_description) {
        this.image_description = image_description;
    }

    public String getT_image_lat() {
        return t_image_lat;
    }

    public void setT_image_lat(String t_image_lat) {
        this.t_image_lat = t_image_lat;
    }

    public String getT_image_lng() {
        return t_image_lng;
    }

    public void setT_image_lng(String t_image_lng) {
        this.t_image_lng = t_image_lng;
    }

    public String getT_image_address() {
        return t_image_address;
    }

    public void setT_image_address(String t_image_address) {
        this.t_image_address = t_image_address;
    }
}
