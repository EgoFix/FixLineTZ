package com.example.fixlinetz.classes;

import javafx.beans.property.SimpleStringProperty;

public class Word {
    private SimpleStringProperty num;
    private SimpleStringProperty text;

    public SimpleStringProperty numProperty() {
        if (num == null) {
            num = new SimpleStringProperty(this, "num");
        }
        return num;
    }

    public SimpleStringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    public Word(String num, String text){
        this.num = new SimpleStringProperty(num);
        this.text = new SimpleStringProperty(text);
    }

    public String getNum() {
        return num.get();
    }

    public void setNum(String num) {
        this.num.set(num);
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
