package org.itnum.transform;

import org.itnum.model.ProductReport;
import org.itnum.model.Sale;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportBuilderTest {

    private final ReportBuilder builder = new ReportBuilder();

    @Test
    void should_return_empty_list_when_no_sales() {
        List<ProductReport> reports = builder.build(List.of());

        assertEquals(0, reports.size());
    }

    @Test
    void should_group_sales_by_product() {
        List<Sale> sales = List.of(
                new Sale("Laptop", "Cotonou", 500000.0, 2, LocalDate.of(2026, 3, 15)),
                new Sale("Laptop", "Parakou", 500000.0, 1, LocalDate.of(2026, 3, 16)),
                new Sale("Smartphone", "Cotonou", 250000.0, 5, LocalDate.of(2026, 3, 17))
        );

        List<ProductReport> reports = builder.build(sales);

        assertEquals(2, reports.size());
    }

    @Test
    void should_sum_quantities_per_product() {
        List<Sale> sales = List.of(
                new Sale("Laptop", "Cotonou", 500000.0, 2, LocalDate.of(2026, 3, 15)),
                new Sale("Laptop", "Parakou", 500000.0, 3, LocalDate.of(2026, 3, 16))
        );

        List<ProductReport> reports = builder.build(sales);

        assertEquals(5, reports.getFirst().totalQuantity());
    }

    @Test
    void should_compute_total_amount_using_unit_price_and_quantity() {
        List<Sale> sales = List.of(
                new Sale("Laptop", "Cotonou", 500000.0, 2, LocalDate.of(2026, 3, 15)),
                new Sale("Laptop", "Parakou", 500000.0, 3, LocalDate.of(2026, 3, 16))
        );

        List<ProductReport> reports = builder.build(sales);

        assertEquals(2_500_000.0, reports.getFirst().totalAmount());
    }

    @Test
    void should_sort_reports_by_total_amount_descending() {
        List<Sale> sales = List.of(
                new Sale("Headphones", "Cotonou", 45000.0, 1, LocalDate.of(2026, 3, 15)),
                new Sale("Laptop", "Cotonou", 500000.0, 1, LocalDate.of(2026, 3, 15)),
                new Sale("Smartphone", "Cotonou", 250000.0, 1, LocalDate.of(2026, 3, 15))
        );

        List<ProductReport> reports = builder.build(sales);

        assertEquals("Laptop", reports.get(0).product());
        assertEquals("Smartphone", reports.get(1).product());
        assertEquals("Headphones", reports.get(2).product());
    }
}