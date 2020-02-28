package com.example.colors;

class ColorListElem {
    private String text;
    private int position;
    private int color;

    public ColorListElem(String text, int position, int color) {
        this.text = text;
        this.position = position;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
