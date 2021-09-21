package com.ashwin.jmockit;

public class Log {
    public static int d(String tag, String msg) {
        System.out.println(tag + " :" + msg);
        return 1;
    }
}
