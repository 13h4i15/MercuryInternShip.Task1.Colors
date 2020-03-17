package com.internship.colors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

final class ColorListJsonLoader {
    private static final String SAVE_FILE_NAME = "coloredListSave.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeJsonInFile(File filesDir, List<ColorListElement> data) throws IOException {
        objectMapper.writeValue(getFullPath(filesDir), data);
    }

    public static List<ColorListElement> readJsonFromFile(File filesDir) throws IOException {
        return objectMapper.readValue(getFullPath(filesDir), new TypeReference<List<ColorListElement>>() {
        });
    }

    private static File getFullPath(File filesDir) {
        return new File(filesDir, SAVE_FILE_NAME);
    }
}
