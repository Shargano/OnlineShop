package net.thumbtack.onlineshop.dto.response.report;

import java.util.List;

public class ClientReportResponse extends ReportResponse {
    private Integer totalCostOfSales;
    private List<ClientReport> report;

    public ClientReportResponse(Integer totalCostOfSales, List<ClientReport> report) {
        this.totalCostOfSales = totalCostOfSales;
        this.report = report;
    }

    public ClientReportResponse() {
    }

    public Integer getTotalCostOfSales() {
        return totalCostOfSales;
    }

    public void setTotalCostOfSales(Integer totalCostOfSales) {
        this.totalCostOfSales = totalCostOfSales;
    }

    public List<ClientReport> getReport() {
        return report;
    }

    public void setReport(List<ClientReport> report) {
        this.report = report;
    }
}