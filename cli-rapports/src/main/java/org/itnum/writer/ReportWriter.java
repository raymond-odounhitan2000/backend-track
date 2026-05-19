package org.itnum.writer;

import org.itnum.exception.CliException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportWriter {

    public void write(Path path, String content) {
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new CliException("Cannot write to file: " + path + " (" + e.getMessage() + ")", e);
        }
    }
}