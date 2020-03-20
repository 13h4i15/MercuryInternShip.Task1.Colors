package com.internship.colors;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.disposables.DisposableContainer;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

final class ColorListJsonLoader {
    private static final String SAVE_FILE_NAME = "coloredListSave.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static void writeJsonInFile(File filesDir, List<ColorListElement> data){
        try {
            objectMapper.writeValue(getFullPath(filesDir), data);
        }catch (IOException e){
            Log.e("tag", e.toString());
        }
    }

    public static List<ColorListElement> readJsonFromFile(File filesDir){
        try {
            return objectMapper.readValue(getFullPath(filesDir), new TypeReference<List<ColorListElement>>() {
            });
        }catch (IOException e){
            Log.e("tag", e.toString());
            return new ArrayList<>();
        }

    }

    private static File getFullPath(File filesDir) {
        return new File(filesDir, SAVE_FILE_NAME);
    }
}
