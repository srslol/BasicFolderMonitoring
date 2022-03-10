package com.scottmyers.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;

public class FolderThreads extends Thread {
    final private String pathName;
    public static boolean isCtrlCPressed = false;

    public FolderThreads(String pathName){
        this.pathName = pathName;
    }

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        System.out.println("Current Thread Name: " + Thread.currentThread().getName());
        System.out.println("Current Thread ID: " + Thread.currentThread().getId());

        try {
            WatchService watchService = null;
            watchService = FileSystems.getDefault().newWatchService();

            // Path path = Paths.get(System.getProperty("user.home"));
            Path path = Paths.get(pathName);

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("-------------------------------------------");
                    System.out.println("Event kind   : " + event.kind());
                    System.out.println("Thread Name  : " + Thread.currentThread().getName());
                    System.out.println("File affected: " + path + "/" + event.context() + "");
                    System.out.println("-------------------------------------------");
                }
                key.reset();
            }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
    }

