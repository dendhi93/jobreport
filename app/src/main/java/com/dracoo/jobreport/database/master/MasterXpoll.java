package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "xpoll")
public class MasterXpoll implements Serializable {

    @DatabaseField(id = true)
    private Integer id_xpoll;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "connection_type")
    private String connection_type;
    @DatabaseField(columnName = "sat")
    private String sat;
    @DatabaseField(columnName = "transponder")
    private String transponder;
    @DatabaseField(columnName = "lft")
    private String lft;
    @DatabaseField(columnName = "cn")
    private String cn;
    @DatabaseField(columnName = "cpi")
    private String cpi;
    @DatabaseField(columnName = "asi")
    private String asi;
    @DatabaseField(columnName = "insert_time")
    private String insert_time;
    @DatabaseField(columnName = "op")
    private String op;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;

    public Integer getId_xpoll() {
        return id_xpoll;
    }

    public void setId_xpoll(Integer id_xpoll) {
        this.id_xpoll = id_xpoll;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getProgress_type() {
        return progress_type;
    }

    public void setProgress_type(String progress_type) {
        this.progress_type = progress_type;
    }

    public String getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getTransponder() {
        return transponder;
    }

    public void setTransponder(String transponder) {
        this.transponder = transponder;
    }

    public String getLft() {
        return lft;
    }

    public void setLft(String lft) {
        this.lft = lft;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getCpi() {
        return cpi;
    }

    public void setCpi(String cpi) {
        this.cpi = cpi;
    }

    public String getAsi() {
        return asi;
    }

    public void setAsi(String asi) {
        this.asi = asi;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
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
