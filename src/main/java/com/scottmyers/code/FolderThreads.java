package com.scottmyers.code;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.file.Files.move;

public class FolderThreads extends Thread {
    final private String pathName;
    public static FileWriter logFile ;
    public String newPath;
    public static boolean isCtrlCPressed = false;

    public FolderThreads(String pathName, String newPath) throws IOException {

        this.pathName = pathName;
        this.newPath = newPath;
        // this.logFile = logFile;
    }

    @Override
    public void run() {
        try {
            String logPath = "/tmp/";
            logFile = new FileWriter(logPath + Thread.currentThread().getName()+".log");
            BufferedWriter logWriter = new BufferedWriter(logFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line = "---------------------------------\n";
            Date d1 = new Date();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
            String formattedDate = df.format(d1);
            System.out.println(
                    // "Date & Time        : "+ formattedDate +
                    "\nCurrent Thread Name: " + Thread.currentThread().getName() +
                    // "\nCurrent Thread ID  : " + Thread.currentThread().getId() +
                    "\nCurrently Watching : " + pathName+
                    "\n");
            logWriter.write(formattedDate + "|START|" +"Thread Name:" + Thread.currentThread().getName() +
                    "|Thread ID:" + Thread.currentThread().getId() +
                    "|Watching:" + pathName+
                    "\n");
            //logWriter.newLine();
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
                Date d2 = new Date();
                SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
                String formattedDate2 = df.format(d1);
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("-------------------------------------------");

                    if (String.valueOf(event.kind())==("ENTRY_CREATE")) {
                        String original = (path + "/" + event.context());
                        String finalDest = (newPath+ "/" + event.context());
                        File source = new File(original);
                        File target = new File(finalDest);
                        Files.copy(source.toPath(), target.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                        logWriter.write(formattedDate2+"|EVENT|Event kind   : " + event.kind() +
                                "|Thread Name:" + Thread.currentThread().getName() +
                                "|File affected:" + path + "/" + event.context() +
                                "|Copying to:" + finalDest + "\n"
                                    );
                        //logWriter.newLine();
                        System.out.println("Event kind      : " + event.kind());
                        System.out.println("Thread Name     : " + Thread.currentThread().getName());
                        System.out.println("Source          : " + path + "/" + event.context() + "");
                        System.out.println("Target          : " + finalDest);
                        Path file = Paths.get(target.getAbsolutePath());
                        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

                        System.out.println("creationTime    : " + attr.creationTime());
                        System.out.println("lastAccessTime  : " + attr.lastAccessTime());
                        System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
                        System.out.println("isDirectory     : " + attr.isDirectory());
                        System.out.println("isOther         : " + attr.isOther());
                        System.out.println("isRegularFile   : " + attr.isRegularFile());
                        System.out.println("isSymbolicLink  : " + attr.isSymbolicLink());
                        System.out.println("Size            : " + attr.size());
                    }

                    if (String.valueOf(event.kind())==("ENTRY_MODIFY") || (String.valueOf(event.kind())==("ENTRY_DELETE"))) {
                        logWriter.write(formattedDate2+"|EVENT|Event kind:" + event.kind());
                        /*+
                                "|Thread Name:" + Thread.currentThread().getName() +
                                "|File affected:" + path + "/" + event.context() + "\n");*/
                       // logWriter.newLine();
                        }

                    /*System.out.println("Event kind   : " + event.kind() +
                            "| Thread Name  : " + Thread.currentThread().getName() +
                            "| File affected: " + path + "/" + event.context() + "\n");
                    System.out.println("-------------------------------------------");*/
                }
                key.reset();
                logWriter.close();
            }

            } catch (IOException e) {
                System.out.println("IO Exception!");
                e.printStackTrace();
            System.exit(0);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }








    }

