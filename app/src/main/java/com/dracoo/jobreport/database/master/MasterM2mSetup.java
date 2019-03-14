package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "m2m_setup")
public class MasterM2mSetup implements Serializable {

    @DatabaseField(id = true)
    private Integer id_setup;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "brand_type_m2m")
    private String brand_type_m2m;
    @DatabaseField(columnName = "sn_m2m")
    private String sn_m2m;
    @DatabaseField(columnName = "brand_type_adaptor")
    private String brand_type_adaptor;
    @DatabaseField(columnName = "sn_adaptor")
    private String sn_adaptor;
    @DatabaseField(columnName = "sim_card1_type")
    private String sim_card1_type;
    @DatabaseField(columnName = "sim_card1_sn")
    private String sim_card1_sn;
    @DatabaseField(columnName = "sim_card1_puk")
    private String sim_card1_puk;
    @DatabaseField(columnName = "sim_card2_type")
    private String sim_card2_type;
    @DatabaseField(columnName = "sim_card_puk")
    private String sim_card_puk;
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


    public Integer getId_setup() {
        return id_setup;
    }

    public void setId_setup(Integer id_setup) {
        this.id_setup = id_setup;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public String getBrand_type_m2m() {
        return brand_type_m2m;
    }

    public void setBrand_type_m2m(String brand_type_m2m) {
        this.brand_type_m2m = brand_type_m2m;
    }

    public String getSn_m2m() {
        return sn_m2m;
    }

    public void setSn_m2m(String sn_m2m) {
        this.sn_m2m = sn_m2m;
    }

    public String getBrand_type_adaptor() {
        return brand_type_adaptor;
    }

    public void setBrand_type_adaptor(String brand_type_adaptor) {
        this.brand_type_adaptor = brand_type_adaptor;
    }

    public String getSn_adaptor() {
        return sn_adaptor;
    }

    public void setSn_adaptor(String sn_adaptor) {
        this.sn_adaptor = sn_adaptor;
    }

    public String getSim_card1_type() {
        return sim_card1_type;
    }

    public void setSim_card1_type(String sim_card1_type) {
        this.sim_card1_type = sim_card1_type;
    }

    public String getSim_card1_sn() {
        return sim_card1_sn;
    }

    public void setSim_card1_sn(String sim_card1_sn) {
        this.sim_card1_sn = sim_card1_sn;
    }

    public String getSim_card1_puk() {
        return sim_card1_puk;
    }

    public void setSim_card1_puk(String sim_card1_puk) {
        this.sim_card1_puk = sim_card1_puk;
    }

    public String getSim_card2_type() {
        return sim_card2_type;
    }

    public void setSim_card2_type(String sim_card2_type) {
        this.sim_card2_type = sim_card2_type;
    }

    public String getSim_card_puk() {
        return sim_card_puk;
    }

    public void setSim_card_puk(String sim_card_puk) {
        this.sim_card_puk = sim_card_puk;
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
