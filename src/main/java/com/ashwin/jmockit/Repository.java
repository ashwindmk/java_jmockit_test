package com.ashwin.jmockit;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private Source mSource;

//    public Repository() {
//        this(new Source());
//    }

    public Repository(Source source) {
        Log.d(Constant.APP_TAG,"Repository( " + source + " )");
        mSource = source;
    }

    public static final String getAppName() {
        return "unit-test-sandbox";
    }

    public static String getName() {
        return "mock-repository";
    }

    public String getSourceName() {
        return Source.getName();
    }

    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public List<String> getWordsSync() {
        Log.d(Constant.APP_TAG,"Repository: getWordsAsync() on thread: " + Thread.currentThread().getName());
        return mSource.getWords();
    }

    public void getWordsAsync(CompletionHandler handler) {
        Log.d(Constant.APP_TAG,"Repository: getWordsAsync()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constant.APP_TAG,"Repository: getWordsAsync: start on thread: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> words = mSource.getWords();
                Log.d(Constant.APP_TAG,"Repository: getWordsAsync: handler.onComplete( " + words + " )");
                handler.onComplete(words);
            }
        }).start();
    }

    public List<String> getWordsContainSync(String contain) throws RuntimeException {
        Log.d(Constant.APP_TAG,"Repository: getWordsContainSync() on thread: " + Thread.currentThread().getName());
        List<String> words = mSource.getWords();
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (word.startsWith(contain)) {
                result.add(word);
            }
        }
        if (result.isEmpty()) {
            throw new RuntimeException("No words found for " + contain);
        }
        return result;
    }

    public void getWordsContainAsync(String contain, CompletionHandler handler) throws RuntimeException {
        Log.d(Constant.APP_TAG,"Repository: getWordsContainAsync()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constant.APP_TAG,"Repository: getWordsContainAsync: start on thread: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String> words = mSource.getWords();
                List<String> result = new ArrayList<>();
                for (String w : words) {
                    if (w.contains(contain)) {
                        result.add(w);
                    }
                }
                if (result.isEmpty()) {
                    throw new RuntimeException("No words found for " + contain);
                }
                Log.d(Constant.APP_TAG,"Repository: getWordsContainAsync: handler.onComplete( " + result + " )");
                handler.onComplete(result);
            }
        }).start();
    }

    public final void onClose() {
        Log.d(Constant.APP_TAG,"Repository: onClose()");
        clear();
    }

    private void clear() {
        Log.d(Constant.APP_TAG,"Repository: clear()");
    }
}
