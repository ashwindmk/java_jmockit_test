package com.ashwin.jmockit;

import java.util.List;

public class Presenter implements Observer {
    public static void main(String[] args) {
        Presenter presenter = new Presenter(new Service(new Repository(new Source())));
        presenter.setObserver();
        presenter.loadWords();
    }

    public Presenter() {
        System.out.println("Presenter()");
    }

    private Service mService;

    public Presenter(Service service) {
        System.out.println("Presenter(service)");
        mService = service;
    }

    public void setObserver() {
        mService.setObserver(this);
    }

    public void loadWords() {
        mService.getWordsSync();
    }

    @Override
    public void onChange(List<String> words) {
        System.out.println("Presenter: onChange( " + words + " ) on thread: " + Thread.currentThread().getName());
    }
}

