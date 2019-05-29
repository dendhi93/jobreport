package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_user_access")
public class MasterUserAccess implements Serializable {

    @DatabaseField(id = true)
    private Integer id_user_list;
    @DatabaseField(columnName = "ua_username")
    private String ua_username;
    @DatabaseField(columnName = "ua_password")
    private String ua_password;
    @DatabaseField(columnName = "ua_name")
    private String ua_name;
    @DatabaseField(columnName = "ua_insert_date")
    private String ua_insert_date;
    @DatabaseField(columnName = "ua_update_date")
    private String ua_update_date;

    public Integer getId_user_list() {
        return id_user_list;
    }

    public void setId_user_list(Integer id_user_list) {
        this.id_user_list = id_user_list;
    }

    public String getUa_username() {
        return ua_username;
    }

    public void setUa_username(String ua_username) {
        this.ua_username = ua_username;
    }

    public String getUa_password() {
        return ua_password;
    }

    public void setUa_password(String ua_password) {
        this.ua_password = ua_password;
    }

    public String getUa_name() {
        return ua_name;
    }

    public void setUa_name(String ua_name) {
        this.ua_name = ua_name;
    }

    public String getUa_insert_date() {
        return ua_insert_date;
    }

    public void setUa_insert_date(String ua_insert_date) {
        this.ua_insert_date = ua_insert_date;
    }

    public String getUa_update_date() {
        return ua_update_date;
    }

    public void setUa_update_date(String ua_update_date) {
        this.ua_update_date = ua_update_date;
    }
}
