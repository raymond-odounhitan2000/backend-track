package org.itnum.parser;

import org.itnum.model.Sale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalesReaderTest {

    private final SalesReader reader = new SalesReader();

    @Test
    void should_read_sales_from_csv_file(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("test-sales.csv");
        String csvContent = """
                product,region,unitPrice,quantity,date
                Laptop,Cotonou,500000.00,2,2026-03-15
                Smartphone,Porto-Novo,250000.00,5,2026-03-15""";
        Files.writeString(csvFile, csvContent);

        List<Sale> sales = reader.read(csvFile);

        assertEquals(2, sales.size());

        Sale first = sales.getFirst();
        assertEquals("Laptop", first.product());
        assertEquals("Cotonou", first.region());
        assertEquals(500000.0, first.unitPrice());
        assertEquals(2, first.quantity());
        assertEquals(LocalDate.of(2026, 3, 15), first.date());
    }
}