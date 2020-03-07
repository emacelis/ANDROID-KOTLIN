package com.realstatey.emmateamappnotes;

public class Notes {
    public String id;
    public String name;
    public int count;
    //cuentos elementos de la nota

    //Trabajar con un contruscctor vacio
    public  Notes(){
        super();
    }


    public Notes(String id, String name, int count){
        this.id=id;
        this.name=name;
        this.count=count;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



}
