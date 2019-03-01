package com.dracoo.jobreport.database.master;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_env")
public class MasterEnvirontment implements Serializable {

    @DatabaseField(id = true)
    private Integer id_env;
    @DatabaseField(columnName = "id_site")
    private Integer id_site;
    @DatabaseField(columnName = "tegangan_pln")
    private Integer tegangan_pln;
    @DatabaseField(columnName = "grounding_pln")
    private Integer grounding_pln;
    @DatabaseField(columnName = "tegangan_ups")
    private Integer tegangan_ups;
    @DatabaseField(columnName = "grounding_ups")
    private Integer grounding_ups;
    @DatabaseField(columnName = "notes")
    private String notes;
    @DatabaseField(columnName = "suhu")
    private Integer suhu;
    @DatabaseField(columnName = "notes_ac")
    private String notes_ac;
    @DatabaseField(columnName = "progress_type")
    private String progress_type;

    public Integer getId_env() {
        return id_env;
    }

    public void setId_env(Integer id_env) {
        this.id_env = id_env;
    }

    public Integer getId_site() {
        return id_site;
    }

    public void setId_site(Integer id_site) {
        this.id_site = id_site;
    }

    public Integer getTegangan_pln() {
        return tegangan_pln;
    }

    public void setTegangan_pln(Integer tegangan_pln) {
        this.tegangan_pln = tegangan_pln;
    }

    public Integer getGrounding_pln() {
        return grounding_pln;
    }

    public void setGrounding_pln(Integer grounding_pln) {
        this.grounding_pln = grounding_pln;
    }

    public Integer getTegangan_ups() {
        return tegangan_ups;
    }

    public void setTegangan_ups(Integer tegangan_ups) {
        this.tegangan_ups = tegangan_ups;
    }

    public Integer getGrounding_ups() {
        return grounding_ups;
    }

    public void setGrounding_ups(Integer grounding_ups) {
        this.grounding_ups = grounding_ups;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getSuhu() {
        return suhu;
    }

    public void setSuhu(Integer suhu) {
        this.suhu = suhu;
    }

    public String getNotes_ac() {
        return notes_ac;
    }

    public void setNotes_ac(String notes_ac) {
        this.notes_ac = notes_ac;
    }

    public String getProgress_type() {
        return progress_type;
    }

    public void setProgress_type(String progress_type) {
        this.progress_type = progress_type;
    }
}
