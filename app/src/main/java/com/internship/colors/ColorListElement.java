package com.internship.colors;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
final class ColorListElement {
    private final int colorId, number;

    @JsonCreator
    public ColorListElement(@JsonProperty("colorId") int colorId, @JsonProperty("number") int number) {
        this.colorId = colorId;
        this.number = number;
    }

    public int getColorId() {
        return colorId;
    }

    public int getNumber() {
        return number;
    }

    public enum ElementColorState {
        RED(R.color.red, R.string.red_color_name),
        ORANGE(R.color.orange, R.string.orange_color_name),
        YELLOW(R.color.yellow, R.string.yellow_color_name),
        GREEN(R.color.green, R.string.green_color_name),
        BLUE(R.color.blue, R.string.blue_color_name),
        NAVY_BLUE(R.color.navy_blue, R.string.navy_blue_color_name),
        LILAC(R.color.lilac, R.string.lilac_color_name),
        BLANK(R.color.transparent, R.string.transparent_color_name);

        @ColorRes
        private final int colorId;
        @StringRes
        private final int colorNameId;

        ElementColorState(int colorId, int colorNameId) {
            this.colorId = colorId;
            this.colorNameId = colorNameId;
        }

        public int getColorId() {
            return colorId;
        }

        public int getColorNameId() {
            return colorNameId;
        }
    }
}
