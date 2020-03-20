package com.internship.colors;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class ColorListJsonLoader {
    private static final String SAVE_FILE_NAME = "coloredListSave.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeJsonInFile(File filesDir, List<ColorListElement> data) {
        try {
            objectMapper.writeValue(getFullPath(filesDir), data);
        } catch (IOException e) {
            Log.e(Constants.LOADING_COLOR_LIST_FILE_ERROR_TAG, e.toString());
        }
    }

    public static List<ColorListElement> readJsonFromFile(File filesDir) {
        try {
            return objectMapper.readValue(getFullPath(filesDir), new TypeReference<List<ColorListElement>>() {
            });
        } catch (IOException e) {
            Log.e(Constants.LOADING_COLOR_LIST_FILE_ERROR_TAG, e.toString());
            return new ArrayList<>();
        }
    }

    private static File getFullPath(File filesDir) {
        return new File(filesDir, SAVE_FILE_NAME);
    }
}
