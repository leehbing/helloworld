package com.it18zhang.avro.test.model;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by hongbing.li on 2017/8/20.
 */
public class MyUser implements Writable,Serializable {
    private static final long serialVersionUID = -1536289812397141151L;
    private String name;
    private Integer favarite_number;
    private String favarite_color;

    public MyUser (String name,Integer favarite_number,String favarite_color){
        super();
        this.name = name;
        this.favarite_number = favarite_number;
        this.favarite_color = favarite_color;
    }
    public MyUser(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFavarite_number() {
        return favarite_number;
    }

    public void setFavarite_number(Integer favarite_number) {
        this.favarite_number = favarite_number;
    }

    public String getFavarite_color() {
        return favarite_color;
    }

    public void setFavarite_color(String favarite_color) {
        this.favarite_color = favarite_color;
    }

    public void write(DataOutput out)throws IOException{
        out.writeUTF(name);
        out.writeInt(favarite_number);
        out.writeUTF(favarite_color);
    }
    public void readFields(DataInput in) throws IOException{
        this.name = in.readUTF();
        this.favarite_number = in.readInt();
        this.favarite_color = in.readUTF();
    }

}
