package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_signature")
public class MasterSignature implements Serializable {
    @DatabaseField(id = true)
    private Integer id_sign;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "t_user_type")
    private String t_user_type;
    @DatabaseField(columnName = "t_sign_path")
    private String t_sign_path;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "conn_type")
    private String conn_type;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;
    @DatabaseField(columnName = "un_user")
    private String un_user;

    public Integer getId_sign() {
        return id_sign;
    }

    public void setId_sign(Integer id_sign) {
        this.id_sign = id_sign;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getT_user_type() {
        return t_user_type;
    }

    public void setT_user_type(String t_user_type) {
        this.t_user_type = t_user_type;
    }

    public String getT_sign_path() {
        return t_sign_path;
    }

    public void setT_sign_path(String t_sign_path) {
        this.t_sign_path = t_sign_path;
    }

    public String getProgress_type() {
        return progress_type;
    }

    public void setProgress_type(String progress_type) {
        this.progress_type = progress_type;
    }

    public String getConn_type() {
        return conn_type;
    }

    public void setConn_type(String conn_type) {
        this.conn_type = conn_type;
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

    public String getUn_user() {
        return un_user;
    }

    public void setUn_user(String un_user) {
        this.un_user = un_user;
    }
}
