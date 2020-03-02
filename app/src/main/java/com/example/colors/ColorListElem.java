package com.example.colors;

class ColorListElem {
    // made for expressiveness and future update.

    public enum ItemColorState {
        RED(R.color.red),
        ORANGE(R.color.orange),
        YELLOW(R.color.yellow),
        GREEN(R.color.green),
        BLUE(R.color.blue),
        NAVY_BLUE(R.color.navy_blue),
        LILAC(R.color.lilac),
        BLANK(R.color.transparent);

        private int colorId;

        ItemColorState(int colorId) {
            this.colorId = colorId;
        }

        public int getColorId() {
            return colorId;
        }

        private static int getColorStatesSize() {
            return ItemColorState.values().length;
        }

        public static int getColorByPosition(int position) {
            return ItemColorState.values()[position % getColorStatesSize()].getColorId();
        }
    }


}
