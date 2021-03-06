package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "vsat_replace")
public class MasterVsatReplace implements Serializable {

    @DatabaseField(id = true)
    private Integer id_replace;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "sn_modem")
    private String sn_modem;
    @DatabaseField(columnName = "sn_adaptor")
    private String sn_adaptor;
    @DatabaseField(columnName = "sn_fh")
    private String sn_fh;
    @DatabaseField(columnName = "sn_lnb")
    private String sn_lnb;
    @DatabaseField(columnName = "sn_rfu")
    private String sn_rfu;
    @DatabaseField(columnName = "sn_dip_odu")
    private String sn_dip_odu;
    @DatabaseField(columnName = "sn_dip_idu")
    private String sn_dip_idu;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "connection_type")
    private String connection_type;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;
    @DatabaseField(columnName = "un_user")
    private String un_user;

    public Integer getId_replace() {
        return id_replace;
    }

    public void setId_replace(Integer id_replace) {
        this.id_replace = id_replace;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getSn_modem() {
        return sn_modem;
    }

    public void setSn_modem(String sn_modem) {
        this.sn_modem = sn_modem;
    }

    public String getSn_fh() {
        return sn_fh;
    }

    public void setSn_fh(String sn_fh) {
        this.sn_fh = sn_fh;
    }

    public String getSn_lnb() {
        return sn_lnb;
    }

    public void setSn_lnb(String sn_lnb) {
        this.sn_lnb = sn_lnb;
    }

    public String getSn_rfu() {
        return sn_rfu;
    }

    public void setSn_rfu(String sn_rfu) {
        this.sn_rfu = sn_rfu;
    }

    public String getSn_dip_odu() {
        return sn_dip_odu;
    }

    public void setSn_dip_odu(String sn_dip_odu) {
        this.sn_dip_odu = sn_dip_odu;
    }

    public String getSn_dip_idu() {
        return sn_dip_idu;
    }

    public void setSn_dip_idu(String sn_dip_idu) {
        this.sn_dip_idu = sn_dip_idu;
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

    public String getUn_user() {
        return un_user;
    }

    public void setUn_user(String un_user) {
        this.un_user = un_user;
    }


    public String getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    public String getSn_adaptor() {
        return sn_adaptor;
    }

    public void setSn_adaptor(String sn_adaptor) {
        this.sn_adaptor = sn_adaptor;
    }
}
