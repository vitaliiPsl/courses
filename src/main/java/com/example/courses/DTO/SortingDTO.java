package com.example.courses.DTO;

import java.util.List;

public class SortingDTO {
    private List<String> sortingOptions;
    private List<String> sortingOrderOptions;
    private String appliedSorting;
    private String appliedSortingOrder;

    public List<String> getSortingOptions() {
        return sortingOptions;
    }

    public void setSortingOptions(List<String> sortingOptions) {
        this.sortingOptions = sortingOptions;
    }

    public List<String> getSortingOrderOptions() {
        return sortingOrderOptions;
    }

    public void setSortingOrderOptions(List<String> sortingOrderOptions) {
        this.sortingOrderOptions = sortingOrderOptions;
    }

    public String getAppliedSorting() {
        return appliedSorting;
    }

    public void setAppliedSorting(String appliedSorting) {
        this.appliedSorting = appliedSorting;
    }

    public String getAppliedSortingOrder() {
        return appliedSortingOrder;
    }

    public void setAppliedSortingOrder(String appliedSortingOrder) {
        this.appliedSortingOrder = appliedSortingOrder;
    }
}
