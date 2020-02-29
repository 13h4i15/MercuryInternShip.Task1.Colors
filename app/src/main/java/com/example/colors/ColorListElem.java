package com.example.colors;

class ColorListElem {
    // Оставлено для выразительности и наличия не задания 1, а 1.1
    // Думаю тут будут какие-то уникальные элементы, которые нужно сохранять.

    public enum ItemColorState{
        RED(R.color.color_red),
        ORANGE(R.color.color_orange),
        YELLOW(R.color.color_yellow),
        GREEN(R.color.color_green),
        BLUE(R.color.color_blue),
        NAVY_BLUE(R.color.color_navy_blue),
        LILAC(R.color.color_lilac),
        BLANK(R.color.color_transparent);

        private int colorId;

        ItemColorState(int colorId) {
            this.colorId = colorId;
        }

        public int getColorId() {
            return colorId;
        }

        private static int getColorStatesSize(){
            return ItemColorState.values().length;
        }

        public static int getColorByPosition(int position){
            return ItemColorState.values()[position % getColorStatesSize()].getColorId();
        }
    }


}
