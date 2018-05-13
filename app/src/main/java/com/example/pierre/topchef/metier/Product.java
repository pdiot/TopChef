package com.example.pierre.topchef.metier;

import android.media.Image;

import java.util.Objects;

public class Product {
    private String name;
    private String taste;
    private int id;

    public Product (String name) {
        this.name = name;
        this.id = -1;
        this.taste = "";
    }

    public Product (String name, String taste) {
        this.name = name;
        this.taste = taste;
        this.id = -1;
    }


    public Product (int id, String name, String taste) {
        this.name = name;
        this.taste = taste;
        this.id = id;
    }

    public Product (int id, String name) {
        this.name = name;
        this.taste = "";
        this.id = id;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", taste='" + taste + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(name, product.name) &&
                Objects.equals(taste, product.taste);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, taste, id);
    }
}
