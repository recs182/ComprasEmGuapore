package br.com.macropublicidade.comprasemguapore.models;

import java.io.Serializable;

/**
 * Created by renan on 8/28/2016.
 */
public class Group implements Serializable{

    Long id;
    String name;
    String id_sector;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_sector() {
        return id_sector;
    }

    public void setId_sector(String id_sector) {
        this.id_sector = id_sector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
