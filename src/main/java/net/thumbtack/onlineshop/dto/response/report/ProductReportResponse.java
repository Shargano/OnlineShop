package net.thumbtack.onlineshop.dto.response.report;

import java.util.List;

public class ProductReportResponse extends ReportResponse {
    private Integer totalCountOfSales;
    private List<ProductReport> report;

    public ProductReportResponse(Integer totalCountOfSales, List<ProductReport> report) {
        this.totalCountOfSales = totalCountOfSales;
        this.report = report;
    }

    public ProductReportResponse() {
    }

    public Integer getTotalCountOfSales() {
        return totalCountOfSales;
    }

    public void setTotalCountOfSales(Integer totalCountOfSales) {
        this.totalCountOfSales = totalCountOfSales;
    }

    public List<ProductReport> getReport() {
        return report;
    }

    public void setReport(List<ProductReport> report) {
        this.report = report;
    }
}