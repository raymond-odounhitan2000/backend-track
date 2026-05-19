package org.itnum.writer;

import org.itnum.model.ProductReport;

import java.util.List;
import java.util.stream.Collectors;

public class MarkdownFormatter implements ReportFormatter {

    private static final String TITLE = "# Sales Report";
    private static final String HEADER = "| Product | Total Quantity | Total Amount |";
    private static final String SEPARATOR = "|---------|----------------|--------------|";

    @Override
    public String format(List<ProductReport> reports) {
        String rows = reports.stream()
                .map(this::formatRow)
                .collect(Collectors.joining("\n"));

        return TITLE + "\n\n" + HEADER + "\n" + SEPARATOR + "\n" + rows;
    }

    private String formatRow(ProductReport report) {
        return "| " + report.product()
                + " | " + report.totalQuantity()
                + " | " + report.totalAmount()
                + " |";
    }
}