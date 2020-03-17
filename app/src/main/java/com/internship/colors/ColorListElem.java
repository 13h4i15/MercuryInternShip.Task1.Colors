package com.internship.colors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
class ColorListElem {
    private int color, number;

    @JsonCreator
    public ColorListElem(@JsonProperty("color") int color, @JsonProperty("number") int number) {
        this.color = color;
        this.number = number;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public enum ItemColorState {
        RED(R.color.red, R.string.red_color_name),
        ORANGE(R.color.orange, R.string.orange_color_name),
        YELLOW(R.color.yellow, R.string.yellow_color_name),
        GREEN(R.color.green, R.string.green_color_name),
        BLUE(R.color.blue, R.string.blue_color_name),
        NAVY_BLUE(R.color.navy_blue, R.string.navy_blue_color_name),
        LILAC(R.color.lilac, R.string.lilac_color_name),
        BLANK(R.color.transparent, R.string.transparent_color_name);

        private final int colorId;
        private final int colorName;

        ItemColorState(int colorId, int colorName) {
            this.colorId = colorId;
            this.colorName = colorName;
        }

        public int getColorId() {
            return colorId;
        }

        public int getColorName() {
            return colorName;
        }

        private static int getColorStatesSize() {
            return ItemColorState.values().length;
        }

        public static ItemColorState getColorByPosition(int position) {
            return ItemColorState.values()[position % getColorStatesSize()];
        }
    }
}
