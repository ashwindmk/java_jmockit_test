package com.ashwin.jmockit;

import java.util.List;

public interface Observer {
    void onChange(List<String> words);
}
