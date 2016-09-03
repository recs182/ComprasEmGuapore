package br.com.macropublicidade.comprasemguapore.models;

import java.io.Serializable;

/**
 * Created by renan on 8/28/2016.
 */
public class Sector implements Serializable{

    Long id;
    String name;

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

    @Override
    public String toString(){
        return name;
    }

}
