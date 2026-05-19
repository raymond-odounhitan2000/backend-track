package org.itnum;

import org.itnum.cli.ReportCommand;
import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ReportCommand()).execute(args);
        System.exit(exitCode);
    }
}