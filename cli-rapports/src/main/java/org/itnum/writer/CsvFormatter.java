package org.itnum.writer;

import org.itnum.model.ProductReport;

import java.util.List;
import java.util.stream.Collectors;

public class CsvFormatter implements ReportFormatter {

    private static final String HEADER = "product,totalQuantity,totalAmount";

    @Override
    public String format(List<ProductReport> reports) {
        String rows = reports.stream()
                .map(this::formatRow)
                .collect(Collectors.joining("\n"));

        return HEADER + "\n" + rows;
    }

    private String formatRow(ProductReport report) {
        return report.product() + "," + report.totalQuantity() + "," + report.totalAmount();
    }
}