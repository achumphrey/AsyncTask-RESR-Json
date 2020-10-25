package com.example.asynctaskrestjson.data;

public class Image {
    public String original;

    Image(String original){
        this.original = original;
    }

    @Override
    public String toString(){

        return String.format("\noriginal: " + original);

    }
}

