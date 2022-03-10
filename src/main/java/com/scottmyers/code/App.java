package com.scottmyers.code;




import java.io.IOException;
import org.apache.commons.cli.*;

public class App
{

    public static final Option ARG_WATCH = new Option("w", "watch",true, "Set function to Watch Folder. (Ex: -w /tmp/)");

    public static String watchOption;

    public static void main( String[] args ) throws IOException, InterruptedException, ParseException {
        Options options = new Options();
        options.addOption(ARG_WATCH);
        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args);

        String[] pathList = new String[args.length];

        if (cl.getOptionValue("filename") != null) {
            watchOption = cl.getOptionValue("filename");
        } else {
            watchOption = "filename";
        }
        for (String path : args) {
            System.out.println("Watching: "+ path);
            startMe(path);
        }
        System.out.println("Running...");
    }

    public static void startMe(String path) {
        FolderThreads folderThread = new FolderThreads(path);
        folderThread.start();
    }
}




