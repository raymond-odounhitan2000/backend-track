package org.itnum.cli;

import org.itnum.exception.CliException;
import org.itnum.format.Format;
import org.itnum.model.ProductReport;
import org.itnum.model.Sale;
import org.itnum.parser.SalesReader;
import org.itnum.transform.ReportBuilder;
import org.itnum.writer.CsvFormatter;
import org.itnum.writer.MarkdownFormatter;
import org.itnum.writer.ReportFormatter;
import org.itnum.writer.ReportWriter;
import org.itnum.writer.TextFormatter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
        name = "cli-rapports",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Generate sales reports from CSV files."
)
public class ReportCommand implements Callable<Integer> {

    @Option(
            names = {"-i", "--input"},
            description = "Input CSV file",
            required = true
    )
    private Path input;

    @Option(
            names = {"-o", "--output"},
            description = "Output file",
            required = true
    )
    private Path output;

    @Option(
            names = {"-f", "--format"},
            description = "Output format: ${COMPLETION-CANDIDATES}",
            required = true
    )
    private Format format;

    @Override
    public Integer call() {
        try {
            SalesReader reader = new SalesReader();
            ReportBuilder builder = new ReportBuilder();
            ReportWriter writer = new ReportWriter();

            ReportFormatter formatter = pickFormatter(format);

            List<Sale> sales = reader.read(input);
            List<ProductReport> reports = builder.build(sales);
            String content = formatter.format(reports);
            writer.write(output, content);

            System.out.println("Report written to: " + output.toAbsolutePath());

            return 0;

        } catch (CliException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return 2;
        }
    }

    private ReportFormatter pickFormatter(Format format) {
        return switch (format) {
            case CSV      -> new CsvFormatter();
            case MARKDOWN -> new MarkdownFormatter();
            case TEXT     -> new TextFormatter();
        };
    }
}