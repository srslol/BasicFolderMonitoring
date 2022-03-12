package com.scottmyers.code;

// import java.io.IOException;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App
{
    public static final Option ARG_WATCH = new Option("w", "watch",true, "Set function to Watch Folder with Automation. (Ex: -w /tmp/)");
    public static final Option ARG_MONITOR = new Option("m", "monitor",true, "Set function to Monitor Folder. (Ex: -w /tmp/)");
    public static final Option ARG_HELP = new Option("h", "help",false, "Show help for command.");

    public static Options options = new Options();

    public static void main( String[] args ) throws InterruptedException, ParseException {

            ARG_WATCH.setArgs(Option.UNLIMITED_VALUES);
            options.addOption(ARG_WATCH);
            options.addOption(ARG_MONITOR);
            options.addOption(ARG_HELP);

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
                String basicList;
                // StringBuffer strbuffer = new StringBuffer();
                basicList = (cmd.getOptionValue("w"));
                String[] argsList = basicList.split(",");

                for (String strBasic : argsList) {
                    System.out.println("Basic: "+ strBasic);

                    try {
                        Path path = Paths.get(strBasic.toString());
                        if (!Files.notExists(path)) {startMe(strBasic);}
                        else {
                            System.out.println("!Folder Error:"+ path);
                            printHelp(options);
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
                // StringBuffer strbuffer = new StringBuffer();
                basicList = (cmd.getOptionValue("m"));
                String[] argsList = basicList.split(",");

                for (String strBasic : argsList) {
                    System.out.println("Basic: "+ strBasic);

                    try {
                        Path path = Paths.get(strBasic.toString());
                        if (!Files.notExists(path)) {startMe(strBasic);}
                        else {
                            System.out.println("!Folder Error:"+ path);
                            printHelp(options);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Folder Error");
                        // printHelp(options);
                        System.exit(0);
                    }
                }
            }
        System.out.println("----------------");
    }


    public static void startMe(String path) {
        FolderThreads folderThread = new FolderThreads(path);
        folderThread.start();
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("\nFolder Monitor v.0.5");
        pw.println();
        formatter.printUsage(pw, 120, "java [-jar] application[.java] [Option] ");
        formatter.printWrapped(pw, 120, "Example: java App.java -w /tmp/watch-folder ");
        pw.println();
        formatter.printOptions(pw, 120, options, 2, 6);
        pw.close();
    }
}

/*
    CommandLineParser parser = new DefaultParser();
    CommandLine cl = parser.parse(options, args);

    CommandLine cmd = new DefaultParser().parse(options, cl.getArgs()); // new String[] {"-option", "a", "b", "-option", "c", "-dummy"});

    List<String[]> thatOption = new ArrayList<String[]>();
            for (Option opt : cl.getOptions()) {
                    System.out.println("opt: " + opt.getOpt());
                    // System.out.println("list : " + opt.getvaluesList());
                    System.out.println("equals: " + opt.equals(options));

                    thatOption.add(opt.getValues());
                    for (String test :opt.getValues()) {
                    System.out.println("->" + test);
                    }
                    System.out.println(Arrays.asList(opt.getValues()).toString());
                    }

                    System.out.println("size: " + thatOption.size());

                String opt_config = cmd.getOptionValue("watch");
                System.out.println("Folder(s) to watch: " + opt_config);

} catch (Exception e) {
            System.out.println("Values Error");
            printHelp(options);
            e.printStackTrace();
            System.exit(-1);
        }
        finally { System.out.println("Exiting....");}
*/

