package com.example;

import java.io.File;

public class StorageInfo {
    public static long getTotalSpace() {
        File root = new File("/");
        return root.getTotalSpace();
    }

    public static long getFreeSpace() {
        File root = new File("/");
        return root.getFreeSpace();
    }

    public static long getUsedSpace() {
        return getTotalSpace() - getFreeSpace();
    }
}