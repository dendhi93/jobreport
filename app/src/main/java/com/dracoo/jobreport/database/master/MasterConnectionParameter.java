package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "network_parameter")
public class MasterConnectionParameter implements Serializable {

    @DatabaseField(id = true)
    private Integer id_parameter;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "connection_type")
    private String connection_type;
    @DatabaseField(columnName = "lan_parameter")
    private String lan_parameter;
    @DatabaseField(columnName = "lan_subnetmask")
    private String lan_subnetmask;
    @DatabaseField(columnName = "sat_parameter")
    private String sat_parameter;
    @DatabaseField(columnName = "sat_symrate")
    private String sat_symrate;
    @DatabaseField(columnName = "sat_freq")
    private String sat_freq;
    @DatabaseField(columnName = "management_esnmodem")
    private String management_esnmodem;
    @DatabaseField(columnName = "management_gateway")
    private String management_gateway;
    @DatabaseField(columnName = "management_snmp")
    private String management_snmp;
    @DatabaseField(columnName = "ranging_signal")
    private String ranging_signal;
    @DatabaseField(columnName = "ranging_data_rate")
    private String ranging_data_rate;
    @DatabaseField(columnName = "ranging_fec")
    private String ranging_fec;
    @DatabaseField(columnName = "ranging_power")
    private String ranging_power;
    @DatabaseField(columnName = "ranging_esno")
    private String ranging_esno;
    @DatabaseField(columnName = "ranging_cno")
    private String ranging_cno;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;
    @DatabaseField(columnName = "un_user")
    private String un_user;


    public Integer getId_parameter() {
        return id_parameter;
    }

    public void setId_parameter(Integer id_parameter) {
        this.id_parameter = id_parameter;
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

    public String getLan_parameter() {
        return lan_parameter;
    }

    public void setLan_parameter(String lan_parameter) {
        this.lan_parameter = lan_parameter;
    }

    public String getLan_subnetmask() {
        return lan_subnetmask;
    }

    public void setLan_subnetmask(String lan_subnetmask) {
        this.lan_subnetmask = lan_subnetmask;
    }

    public String getSat_parameter() {
        return sat_parameter;
    }

    public void setSat_parameter(String sat_parameter) {
        this.sat_parameter = sat_parameter;
    }

    public String getSat_symrate() {
        return sat_symrate;
    }

    public void setSat_symrate(String sat_symrate) {
        this.sat_symrate = sat_symrate;
    }

    public String getSat_freq() {
        return sat_freq;
    }

    public void setSat_freq(String sat_freq) {
        this.sat_freq = sat_freq;
    }

    public String getManagement_esnmodem() {
        return management_esnmodem;
    }

    public void setManagement_esnmodem(String management_esnmodem) {
        this.management_esnmodem = management_esnmodem;
    }

    public String getManagement_gateway() {
        return management_gateway;
    }

    public void setManagement_gateway(String management_gateway) {
        this.management_gateway = management_gateway;
    }

    public String getManagement_snmp() {
        return management_snmp;
    }

    public void setManagement_snmp(String management_snmp) {
        this.management_snmp = management_snmp;
    }

    public String getRanging_signal() {
        return ranging_signal;
    }

    public void setRanging_signal(String ranging_signal) {
        this.ranging_signal = ranging_signal;
    }

    public String getRanging_data_rate() {
        return ranging_data_rate;
    }

    public void setRanging_data_rate(String ranging_data_rate) {
        this.ranging_data_rate = ranging_data_rate;
    }

    public String getRanging_fec() {
        return ranging_fec;
    }

    public void setRanging_fec(String ranging_fec) {
        this.ranging_fec = ranging_fec;
    }

    public String getRanging_power() {
        return ranging_power;
    }

    public void setRanging_power(String ranging_power) {
        this.ranging_power = ranging_power;
    }

    public String getRanging_esno() {
        return ranging_esno;
    }

    public void setRanging_esno(String ranging_esno) {
        this.ranging_esno = ranging_esno;
    }

    public String getRanging_cno() {
        return ranging_cno;
    }

    public void setRanging_cno(String ranging_cno) {
        this.ranging_cno = ranging_cno;
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
