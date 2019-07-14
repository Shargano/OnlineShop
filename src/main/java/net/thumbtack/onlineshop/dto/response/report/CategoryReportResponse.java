package net.thumbtack.onlineshop.dto.response.report;

import java.util.List;

public class CategoryReportResponse extends ReportResponse {
    private Integer totalCountOfSales;
    private List<CategoryReport> report;

    public CategoryReportResponse(Integer totalCountOfSales, List<CategoryReport> report) {
        this.totalCountOfSales = totalCountOfSales;
        this.report = report;
    }

    public CategoryReportResponse() {
    }

    public Integer getTotalCountOfSales() {
        return totalCountOfSales;
    }

    public void setTotalCountOfSales(Integer totalCountOfSales) {
        this.totalCountOfSales = totalCountOfSales;
    }

    public List<CategoryReport> getReport() {
        return report;
    }

    public void setReport(List<CategoryReport> report) {
        this.report = report;
    }
}