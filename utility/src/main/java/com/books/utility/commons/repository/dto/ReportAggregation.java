package com.books.utility.commons.repository.dto;

import com.books.utility.commons.repository.statics.AggregationType;

import java.util.List;

public class ReportAggregation {
    private List<ReportColumn> aggregationColumn;
    private AggregationType type;

    public ReportAggregation(List<ReportColumn> aggregationColumn, AggregationType type) {
        this.aggregationColumn = aggregationColumn;
        this.type = type;
    }

    public List<ReportColumn> getAggregationColumn() {
        return aggregationColumn;
    }

    public AggregationType getType() {
        return type;
    }
}
