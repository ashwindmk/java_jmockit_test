package com.ashwin.jmockit;

import java.util.ArrayList;
import java.util.List;

public class Source {
    public Source() {
        Log.d(Constant.APP_TAG,"Source()");
    }

    public static String getName() {
        return "fake-source";
    }

    public List<String> getWords() {
        Log.d(Constant.APP_TAG,"Source: getWords() on thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> words = new ArrayList<>();
        words.add("avocado");
        words.add("ball");
        words.add("cat");
        words.add("catalog");
        words.add("cake");
        words.add("cakewalk");
        words.add("cup");
        words.add("cupcake");
        words.add("cupboard");
        words.add("dog");
        words.add("egg");
        words.add("fruit");
        return words;
    }
}
