package org.itnum.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class SalesGenerator {

    private static final List<String> PRODUCTS = List.of(
            "Laptop", "Smartphone", "Tablet", "Headphones", "Monitor",
            "Keyboard", "Mouse", "Printer", "Speaker", "Webcam"
    );

    private static final List<String> REGIONS = List.of(
            "Cotonou", "Porto-Novo", "Parakou", "Abomey", "Bohicon",
            "Natitingou", "Djougou", "Ouidah", "Lokossa", "Kandi"
    );

    private static final List<Double> UNIT_PRICES = List.of(
            500000.0, 250000.0, 150000.0, 45000.0, 200000.0,
            15000.0, 8000.0, 120000.0, 35000.0, 25000.0
    );

    public static void main(String[] args) throws IOException {

        int numberOfLines = 10_000;
        Path output = Path.of("data/sales-10k.csv");

        if (args.length > 0) {
            numberOfLines = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            output = Path.of(args[1]);
        }

        generate(numberOfLines, output);
        System.out.println("Generated " + numberOfLines + " lines into " + output);
    }

    private static void generate(int numberOfLines, Path output) throws IOException {
        Random random = new Random(42);

        StringBuilder sb = new StringBuilder();
        sb.append("product,region,unitPrice,quantity,date\n");

        for (int i = 0; i < numberOfLines; i++) {
            int productIndex = random.nextInt(PRODUCTS.size());
            String product = PRODUCTS.get(productIndex);
            String region = REGIONS.get(random.nextInt(REGIONS.size()));
            double unitPrice = UNIT_PRICES.get(productIndex);
            int quantity = 1 + random.nextInt(10);
            LocalDate date = LocalDate.of(2026, 1, 1).plusDays(random.nextInt(365));

            sb.append(product).append(",")
                    .append(region).append(",")
                    .append(unitPrice).append(",")
                    .append(quantity).append(",")
                    .append(date).append("\n");
        }

        Files.writeString(output, sb.toString());
    }
}