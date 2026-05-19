package org.itnum.model;

public record ProductReport(
        String product,
        int totalQuantity,
        double totalAmount
) { }