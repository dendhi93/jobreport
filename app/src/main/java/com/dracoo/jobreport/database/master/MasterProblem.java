package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_problem")
public class MasterProblem implements Serializable {

    @DatabaseField(id = true)
    private Integer id_problem;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "modem")
    private String modem;
    @DatabaseField(columnName = "action")
    private String action;
    @DatabaseField(columnName = "berangkat")
    private String berangkat;
    @DatabaseField(columnName = "tiba")
    private String tiba;
    @DatabaseField(columnName = "finish")
    private String finish;
    @DatabaseField(columnName = "upline")
    private String upline;
    @DatabaseField(columnName = "online")
    private String online;
    @DatabaseField(columnName = "reason")
    private String reason;
    @DatabaseField(columnName = "closed")
    private String closed;
    @DatabaseField(columnName = "closed_by")
    private String closed_by;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;

    public Integer getId_problem() {
        return id_problem;
    }

    public void setId_problem(Integer id_problem) {
        this.id_problem = id_problem;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getModem() {
        return modem;
    }

    public void setModem(String modem) {
        this.modem = modem;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBerangkat() {
        return berangkat;
    }

    public void setBerangkat(String berangkat) {
        this.berangkat = berangkat;
    }

    public String getTiba() {
        return tiba;
    }

    public void setTiba(String tiba) {
        this.tiba = tiba;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getUpline() {
        return upline;
    }

    public void setUpline(String upline) {
        this.upline = upline;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getClosed_by() {
        return closed_by;
    }

    public void setClosed_by(String closed_by) {
        this.closed_by = closed_by;
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
