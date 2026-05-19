package org.itnum.model;

import java.time.LocalDate;

public record Sale(
        String product,
        String region,
        double unitPrice,
        int quantity,
        LocalDate date
) {
}
