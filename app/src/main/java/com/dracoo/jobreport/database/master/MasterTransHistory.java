package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_trans_history")
public class MasterTransHistory implements Serializable {
    @DatabaseField(id = true)
    private Integer id_trans;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "un_user")
    private String un_user;
    @DatabaseField(columnName = "trans_step")
    private String trans_step;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;
    @DatabaseField(columnName = "is_submited")
    private Integer is_submited;


    public Integer getId_trans() {
        return id_trans;
    }

    public void setId_trans(Integer id_trans) {
        this.id_trans = id_trans;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getUn_user() {
        return un_user;
    }

    public void setUn_user(String un_user) {
        this.un_user = un_user;
    }

    public String getTrans_step() {
        return trans_step;
    }

    public void setTrans_step(String trans_step) {
        this.trans_step = trans_step;
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

    public void setIs_submited(Integer is_submited) {
        this.is_submited = is_submited;
    }
}
