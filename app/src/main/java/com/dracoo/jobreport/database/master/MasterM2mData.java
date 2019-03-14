package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "m2m_data")
public class MasterM2mData implements Serializable {

    @DatabaseField(id = true)
    private Integer id_data;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;
    @DatabaseField(columnName = "username")
    private String username;
    @DatabaseField(columnName = "password")
    private String password;
    @DatabaseField(columnName = "user")
    private String user;
    @DatabaseField(columnName = "remote")
    private String remote;
    @DatabaseField(columnName = "tunnel_id")
    private String tunnel_id;
    @DatabaseField(columnName = "ip_bonding")
    private String ip_bonding;
    @DatabaseField(columnName = "ip_vlan")
    private String ip_vlan;
    @DatabaseField(columnName = "ip_lan")
    private String ip_lan;
    @DatabaseField(columnName = "subnet_mas")
    private String subnet_mas;
    @DatabaseField(columnName = "agg")
    private String agg;
    @DatabaseField(columnName = "connection_type")
    private String connection_type;
    @DatabaseField(columnName = "insert_date")
    private String insert_date;
    @DatabaseField(columnName = "update_date")
    private String update_date;
    @DatabaseField(columnName = "un_user")
    private String un_user;

    public Integer getId_data() {
        return id_data;
    }

    public void setId_data(Integer id_data) {
        this.id_data = id_data;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getTunnel_id() {
        return tunnel_id;
    }

    public void setTunnel_id(String tunnel_id) {
        this.tunnel_id = tunnel_id;
    }

    public String getIp_bonding() {
        return ip_bonding;
    }

    public void setIp_bonding(String ip_bonding) {
        this.ip_bonding = ip_bonding;
    }

    public String getIp_vlan() {
        return ip_vlan;
    }

    public void setIp_vlan(String ip_vlan) {
        this.ip_vlan = ip_vlan;
    }

    public String getIp_lan() {
        return ip_lan;
    }

    public void setIp_lan(String ip_lan) {
        this.ip_lan = ip_lan;
    }

    public String getSubnet_mas() {
        return subnet_mas;
    }

    public void setSubnet_mas(String subnet_mas) {
        this.subnet_mas = subnet_mas;
    }

    public String getAgg() {
        return agg;
    }

    public void setAgg(String agg) {
        this.agg = agg;
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
