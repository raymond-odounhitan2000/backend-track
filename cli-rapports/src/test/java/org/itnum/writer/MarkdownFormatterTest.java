package org.itnum.writer;

import org.itnum.model.ProductReport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkdownFormatterTest {

    private final MarkdownFormatter formatter = new MarkdownFormatter();

    @Test
    void should_format_reports_as_markdown_table() {
        List<ProductReport> reports = List.of(
                new ProductReport("Laptop", 5, 2500000.0),
                new ProductReport("Smartphone", 12, 3000000.0)
        );

        String result = formatter.format(reports);

        String expected = """
                # Sales Report
                
                | Product | Total Quantity | Total Amount |
                |---------|----------------|--------------|
                | Laptop | 5 | 2500000.0 |
                | Smartphone | 12 | 3000000.0 |""";

        assertEquals(expected, result);
    }
}