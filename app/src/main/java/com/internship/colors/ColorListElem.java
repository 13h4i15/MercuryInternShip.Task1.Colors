package com.internship.colors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
class ColorListElem {
    // made for expressiveness and future update.
    private int color, position;

    @JsonCreator
    public ColorListElem(@JsonProperty("color") int color, @JsonProperty("position") int position){
        this.color = color;
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public enum ItemColorState {
        RED(R.color.red),
        ORANGE(R.color.orange),
        YELLOW(R.color.yellow),
        GREEN(R.color.green),
        BLUE(R.color.blue),
        NAVY_BLUE(R.color.navy_blue),
        LILAC(R.color.lilac),
        BLANK(R.color.transparent);

        private final int colorId;

        ItemColorState(int colorId) {
            this.colorId = colorId;
        }

        public int getColorId() {
            return colorId;
        }

        private static int getColorStatesSize() {
            return ItemColorState.values().length;
        }

        public static ItemColorState getColorByPosition(int position) {
            return ItemColorState.values()[position % getColorStatesSize()];
        }
    }
}
