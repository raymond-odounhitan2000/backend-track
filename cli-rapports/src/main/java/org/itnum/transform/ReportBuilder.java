package org.itnum.transform;

import org.itnum.model.ProductReport;
import org.itnum.model.Sale;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportBuilder {

    public List<ProductReport> build(List<Sale> sales) {

        Map<String, List<Sale>> salesByProduct = sales.stream()
                .collect(Collectors.groupingBy(Sale::product));

        return salesByProduct.entrySet().stream()
                .map(this::buildProductReport)
                .sorted(Comparator.comparingDouble(ProductReport::totalAmount).reversed())
                .toList();
    }

    private ProductReport buildProductReport(Map.Entry<String, List<Sale>> entry) {
        String product = entry.getKey();
        List<Sale> productSales = entry.getValue();

        int totalQuantity = productSales.stream()
                .mapToInt(Sale::quantity)
                .sum();

        double totalAmount = productSales.stream()
                .mapToDouble(s -> s.unitPrice() * s.quantity())
                .sum();

        return new ProductReport(product, totalQuantity, totalAmount);
    }
}