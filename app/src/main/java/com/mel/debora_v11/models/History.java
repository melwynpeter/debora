package com.mel.debora_v11.models;

import java.io.Serializable;

public class History implements Serializable {
    public String text;
    public History(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
