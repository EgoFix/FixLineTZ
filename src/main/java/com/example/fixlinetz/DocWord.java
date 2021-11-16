package com.example.fixlinetz;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    private String wordRname; // имя WordR из словаря в соответствии с Word
    private int value; // значение value из словаря
    private String[] attributes; // поля WordR из словаря



    DocWord(String wordName, String size, String wordRname, int value, String[] attributes){
        setWordName(wordName);
        setSize(size);
        setWordRname(wordRname);
        setValue(value);
        setAttributes(attributes);
    }

    DocWord(String wordName,String size){
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
    }

    public String getWordRname() {
        return wordRname;
    }

    public void setWordRname(String wordRname) {
        this.wordName = wordRname;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "DocWord{" +
                "wordName='" + wordName + '\'' +
                ", size=" + size +
                ", wordRname='" + wordRname + '\'' +
                ", value=" + value +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }
}
