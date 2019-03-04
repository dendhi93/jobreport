package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_jobdesc")
public class MasterJobDesc implements Serializable {

    @DatabaseField(id = true)
    private Integer id_jobdesc;
    @DatabaseField(columnName = "name_user")
    private String name_user;
    @DatabaseField(columnName = "user_phone")
    private String user_phone;
    @DatabaseField(columnName = "name_pic")
    private String name_pic;
    @DatabaseField(columnName = "jabatan_desc")
    private String jabatan_desc;
    @DatabaseField(columnName = "pic_phone")
    private String pic_phone;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;

    public Integer getId_jobdesc() {
        return id_jobdesc;
    }

    public void setId_jobdesc(Integer id_jobdesc) {
        this.id_jobdesc = id_jobdesc;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getName_pic() {
        return name_pic;
    }

    public void setName_pic(String name_pic) {
        this.name_pic = name_pic;
    }

    public String getJabatan_desc() {
        return jabatan_desc;
    }

    public void setJabatan_desc(String jabatan_desc) {
        this.jabatan_desc = jabatan_desc;
    }

    public String getPic_phone() {
        return pic_phone;
    }

    public void setPic_phone(String pic_phone) {
        this.pic_phone = pic_phone;
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
}
