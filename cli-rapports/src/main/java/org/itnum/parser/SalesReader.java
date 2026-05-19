package org.itnum.parser;

import org.itnum.exception.CliException;
import org.itnum.model.Sale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

public class SalesReader {

    public List<Sale> read(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .map(this::parseLine)
                    .toList();
        } catch (NoSuchFileException e) {
            throw new CliException("Input file not found: " + path, e);
        } catch (IOException e) {
            throw new CliException("Cannot read file: " + path + " (" + e.getMessage() + ")", e);
        }
    }

    private Sale parseLine(String line) {
        try {
            String[] fields = line.split(",");
            if (fields.length < 5) {
                throw new CliException("Invalid line, expected 5 fields, got " + fields.length + ": " + line);
            }
            return new Sale(
                    fields[0],
                    fields[1],
                    Double.parseDouble(fields[2]),
                    Integer.parseInt(fields[3]),
                    LocalDate.parse(fields[4])
            );
        } catch (NumberFormatException e) {
            throw new CliException("Invalid number in line: " + line, e);
        } catch (DateTimeParseException e) {
            throw new CliException("Invalid date in line: " + line, e);
        }
    }
}