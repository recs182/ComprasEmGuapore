package br.com.macropublicidade.comprasemguapore.models;

import java.io.Serializable;

/**
 * Created by renan on 8/28/2016.
 */
public class Subgroup implements Serializable{
    Long id;
    String name;
    String id_group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_group() {
        return id_group;
    }

    public void setId_group(String id_group) {
        this.id_group = id_group;
    }

    @Override
    public String toString(){
        return name;
    }
}
