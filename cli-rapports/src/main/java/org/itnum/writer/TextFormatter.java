package org.itnum.writer;

import org.itnum.model.ProductReport;

import java.util.List;
import java.util.stream.Collectors;

public class TextFormatter implements ReportFormatter {

    @Override
    public String format(List<ProductReport> reports) {
        String header = String.format("%-15s %15s %15s", "Product", "Quantity", "Amount");
        String separator = "-".repeat(47);

        String rows = reports.stream()
                .map(this::formatRow)
                .collect(Collectors.joining("\n"));

        return header + "\n" + separator + "\n" + rows;
    }

    private String formatRow(ProductReport report) {
        return String.format("%-15s %15d %15.2f",
                report.product(),
                report.totalQuantity(),
                report.totalAmount());
    }
}