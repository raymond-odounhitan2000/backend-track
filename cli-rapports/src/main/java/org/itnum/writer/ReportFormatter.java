package org.itnum.writer;

import org.itnum.model.ProductReport;

import java.util.List;

public interface ReportFormatter {
    String format(List<ProductReport> reports);
}