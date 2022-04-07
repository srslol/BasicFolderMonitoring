package com.scottmyers.code;

import org.apache.commons.cli.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;



public class App
{
    public static final Option ARG_WATCH = new Option("w", "watch",true, "Set function to Watch Folder with Automation.");
    public static final Option ARG_MONITOR = new Option("m", "monitor",true, "Set function to Monitor Folder Only. (Ex: -w /tmp/)");
    public static final Option ARG_HELP = new Option("h", "help",false, "Show help for command.");
    public static final Option ARG_TARGET = new Option("t", "target",true, "Watch & Copy to single target folder.");

    public static FileWriter logFile;

    public static Options options = new Options();

    public static void main( String[] args ) throws IOException, ParseException { // InterruptedException

        initLog();
        ARG_WATCH.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(ARG_WATCH);
        options.addOption(ARG_MONITOR);
        options.addOption(ARG_HELP);
        options.addOption(ARG_TARGET);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        System.out.println("----------------");

        if (cmd.hasOption("h")) {printHelp(options); System.exit(0);}

        if ((cmd.hasOption("w")) && (cmd.hasOption("m"))) {
            System.out.println("Cannot select Watch AND Monitor.");
            printHelp(options);
            System.exit(0);
        }

        if(cmd.hasOption("w")){
            String targetPath =(cmd.getOptionValue("t"));
            if (!Files.exists(Paths.get(targetPath))) {
                System.out.println("Cannot find Target Folder");
                printHelp(options);
                System.exit(0);
            }

            String basicList;
            basicList = (cmd.getOptionValue("w"));
            String[] argsList = basicList.split(",");

            for (String strBasic : argsList) {
                // System.out.println("Watching : "+ strBasic);
                writeLog(strBasic);
                try {
                    Path path = Paths.get(strBasic);
                    if (!Files.notExists(path)) {startMe(strBasic,targetPath);}
                    else {
                        System.out.println("!Folder Error:"+ path);
                        printHelp(options);
                        System.exit(0);
                    }
                }
                catch (Exception e) {
                    System.out.println("Folder Error");
                    // printHelp(options);
                    System.exit(0);
                }
            }
        }

        if (cmd.hasOption("m")) {
            String basicList;
            basicList = (cmd.getOptionValue("m"));
            String[] argsList = basicList.split(",");

            for (String strBasic : argsList) {
                System.out.println("Basic: "+ strBasic + "--> ");

                try {
                    Path path = Paths.get(strBasic);
                    if (!Files.notExists(path)) {startMe(strBasic, "");}
                    else {
                        System.out.println("!Folder Error: Please check path."+ path);
                        printHelp(options);
                        System.exit(0);
                    }
                }
                catch (Exception e) {
                    System.out.println("Folder Path Error");
                    printHelp(options);
                    System.exit(0);
                }
            }
        }
        // else {printHelp(options);};
        // System.out.println("----------------");
        // logFile.close();
    }


    public static void startMe(String sourcePath, String targetPath) throws IOException {

        FolderThreads folderThread = new FolderThreads(sourcePath,targetPath);
        folderThread.start();
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("\nFolder Monitor v.0.5");
        pw.println();
        formatter.printUsage(pw, 120, "java [-jar] application[.java] [Option] ");
        formatter.printWrapped(pw, 120, "Example: java App.java -w /tmp/watch/1 -t /tmp/output/1");
        formatter.printWrapped(pw, 120, "Example: java App.java -w /tmp/watch/1, /tmp/watch/2 -t /tmp/output/1");
        formatter.printWrapped(pw, 120, "Example: java App.java -m /tmp/watch/1");
        pw.println();
        formatter.printOptions(pw, 120, options, 2, 6);
        pw.close();
    }

    public static void initLog() throws IOException {
        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
        SimpleDateFormat dfShort = new SimpleDateFormat("HH-mm-a");
        String formattedDate = df.format(d1);
        String dfShortFormatted = dfShort.format(d1);
        String logPathName = "/var/log/WatchService/WatchService-";
        logFile = new FileWriter(logPathName + dfShortFormatted + ".log",true);
        logFile.write(formattedDate + " --------------- Application Starting ---------------\n");
        System.out.println("Log Initiated: " + logPathName + dfShortFormatted + ".log");
    }

    public static void writeLog(String line) throws IOException {
        logFile.write(line);
    }

}
