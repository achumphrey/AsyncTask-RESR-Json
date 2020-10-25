package com.example.asynctaskrestjson.data;

class Image {
    String original;

    Image(String original){
        this.original = original;
    }

    @Override
    public String toString(){

        return String.format("\noriginal: " + original);

    }
}

