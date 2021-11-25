package com.example.fixlinetz.classes;

import java.util.Arrays;

public class DocWord {
    /**
     * Данный класс служит для обработки данных из словаря:
     * <p>
     * - получение данных по типу Word
     * <pre>
     *  = получение поля name с указанием типа объекта
     *  = получение поля size с указанием количества подтипов объекта
     *  </pre>
     * - получение данных по атрибутам типа WordR, т.е. на уровень ниже Word
     * <pre>
     *  = получение поля name с указанием подтипа объекта
     * </pre>
     *  </p>
     */

    private String wordName; // имя Word из словаря
    private int size; // количество подтипов объекта
    private int value; // значение value из словаря
    private String[] wordRnames; // имена WordR из словаря в соответствии с Word


    DocWord(String wordName, String size, String wordRname, int value, int i){
        setWordName(wordName);
        setSize(size);
        setWordRname(wordRname, i);
        setValue(value);
    }


    public DocWord(String wordName, String size){
        setWordName(wordName);
        setSize(size);
    }


    public String getWordName() {
        return wordName;
    }


    public void setWordName(String wordName) {
        this.wordName = wordName.split("\"")[1];
    }


    public int getSize() {
        return size;
    }


    public void setSize(String size) {
        this.size = Integer.parseInt(size.split("\"")[1]);
        this.wordRnames = new String[this.size];
    }


    public String getWordRname(int i) {
        return wordRnames[i].split("\"")[1];
    }


    public void setWordRname(String wordRname, int i) {
                this.wordRnames[i] = wordRname;
    }


    public int getValue() {
        return value;
    }


    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "DocWord{" +
                "wordName='" + wordName + '\'' +
                ", size=" + size +
                ", value=" + value +
                ", wordRnames=" + Arrays.toString(wordRnames) +
                '}';
    }
}



























