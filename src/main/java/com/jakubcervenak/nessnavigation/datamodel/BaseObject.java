package com.jakubcervenak.nessnavigation.datamodel;

import com.jakubcervenak.nessnavigation.core.Constants;

import java.io.Serializable;


public abstract class BaseObject implements Serializable, Constants {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseObject)) return false;

        BaseObject that = (BaseObject) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

}