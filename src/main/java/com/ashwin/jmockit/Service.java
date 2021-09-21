package com.ashwin.jmockit;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Service {
    private final Repository mRepository;

    private Observer mObserver;

    private CompletionHandler mCompletionHandler = new CompletionHandler() {
        @Override
        public void onComplete(List<String> words) {
            Log.d(Constant.APP_TAG,"completionHandler.onComplete( " + words + " )");
        }
    };

//    public Service() {
//        this(new Repository(new Source()));
//        Log.d(Constant.APP_TAG,"Service()");
//    }

    public Service(Repository repository) {
        Log.d(Constant.APP_TAG,"Service( " + repository + " )");
        mRepository = repository;
    }

    public void setObserver(Observer observer) {
        Log.d(Constant.APP_TAG,"Service: setObserver( " + observer + " )");
        mObserver = observer;
    }

    public void print(String s) {
        Log.d(Constant.APP_TAG,"Service: print( " + s + " )");
    }

    public int add(int a, int b) {
//        return a + b;
        return mRepository.add(a, b);
    }

    public int sub(int a, int b) {
//        return a - b;
        return mRepository.sub(a, b);
    }

    public String getAppName() {
        return mRepository.getAppName();
    }

    public List<String> getWordsSync() {
        return mRepository.getWordsSync();
    }

    public void getWordsAsync() {
        Log.d(Constant.APP_TAG,"Service: getWordsAsync");
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mRepository.getWordsAsync(new CompletionHandler() {
            @Override
            public void onComplete(List<String> words) {
                Log.d(Constant.APP_TAG,"Service: completionHandler.onComplete( " + words + " )");
                if (mObserver != null) {
                    mObserver.onChange(words);
                }
            }
        });
    }

    public List<String> getWordsContainSync(String s) throws RuntimeException {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return mRepository.getWordsContainSync(s);
    }

    public void getWordsContainAsync(String s) throws RuntimeException {
        Log.d(Constant.APP_TAG,"Service: getWordsAsync");
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException();
        }
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mRepository.getWordsContainAsync(s, new CompletionHandler() {
            @Override
            public void onComplete(List<String> words) {
                Log.d(Constant.APP_TAG,"Service: completionHandler.onComplete( " + words + " )");
                if (mObserver != null) {
                    mObserver.onChange(words);
                }
            }
        });
    }

    public String getRepositoryName() {
        return Repository.getName();
    }

    // Uses repository's sync method but make it asynchronous
//    public void getWordsSyncAsync(Context context) {
//        CompletableFuture.supplyAsync(() -> mRepository.getWordsSync())
//                .thenAcceptAsync(words -> {
//                    Log.d(Constant.APP_TAG,"Service: thenAccept( " + words + " ) on thread: " + Thread.currentThread().getName());
//                    if (mObserver != null) {
//                        mObserver.onChange(words);
//                    }
//                }, ContextCompat.getMainExecutor(context));
//        Log.d(Constant.APP_TAG,"Service: getWordsSyncAsync() after supplyAsync on thread: " + Thread.currentThread().getName());
//    }

    public final void close() {
        Log.d(Constant.APP_TAG,"Service: close()");
        mRepository.onClose();
    }
}
