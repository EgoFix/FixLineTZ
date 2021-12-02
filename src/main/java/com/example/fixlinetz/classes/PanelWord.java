package com.example.fixlinetz.classes;


public class PanelWord {
    private String num;
    private String text;


    public PanelWord(String num, String text){
        this.num = num;
        this.text = text;
    }



    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return num + " - " + text;
    }
}
