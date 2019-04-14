package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_action")
public class MasterAction implements Serializable {

    @DatabaseField(id = true)
    private Integer id_action;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "action_date_time")
    private String action_date_time;
    @DatabaseField(columnName = "action_desc")
    private String action_desc;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "conn_type")
    private String conn_type;
    @DatabaseField(columnName = "un_user")
    private String un_user;
    @DatabaseField(columnName = "action_end_time")
    private String action_end_time;

    public Integer getId_action() {
        return id_action;
    }

    public void setId_action(Integer id_action) {
        this.id_action = id_action;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getAction_date_time() {
        return action_date_time;
    }

    public void setAction_date_time(String action_date_time) {
        this.action_date_time = action_date_time;
    }

    public String getAction_desc() {
        return action_desc;
    }

    public void setAction_desc(String action_desc) {
        this.action_desc = action_desc;
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

    public String getUn_user() {
        return un_user;
    }

    public void setUn_user(String un_user) {
        this.un_user = un_user;
    }

    public String getAction_end_time() {
        return action_end_time;
    }

    public void setAction_end_time(String action_end_time) {
        this.action_end_time = action_end_time;
    }
}
