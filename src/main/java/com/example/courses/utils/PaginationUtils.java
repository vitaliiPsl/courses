package com.example.courses.utils;

import com.example.courses.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PaginationUtils {
    private final static int DEFAULT_PAGE = 1;
    private final static int RECORDS_PER_PAGE = 5;

    private PaginationUtils(){}

    public static List<?> applyPagination(List<?> records, HttpServletRequest request) {

        // get current page if it is not null
        int currentPage = getCurrentPage(request);

        // get number of records per page if it is not null
        int recordPerPage = getNumberOfRecordsOnPage(request);

        // calculate total number of pages
        int numberOfPages = getNumberOfPages(records, recordPerPage);

        // redirect to 'not found' if current page is not in range of total number of pages
        if (numberOfPages != 0 && (currentPage < 1 || currentPage > numberOfPages)) {
//            logger.error("Chosen page (" + currentPage + ") does not exists");
            throw new NotFoundException();
        }

        // save current page and number of pages in request
        request.setAttribute("page", currentPage);
        request.setAttribute("records_on_page", recordPerPage);
        request.setAttribute("number_of_pages", numberOfPages);

        // return sublist
        int start = currentPage * recordPerPage - recordPerPage;
        int end = Math.min(start + recordPerPage, records.size());

        return records.subList(start, end);
    }

    private static int getNumberOfPages(List<?> records, int recordPerPage) {
        int numberOfPages = records.size() / recordPerPage;
        if (records.size() % recordPerPage > 0) {
            numberOfPages++;
        }

        return numberOfPages;
    }

    private static int getNumberOfRecordsOnPage(HttpServletRequest request) {
        int recordPerPage = RECORDS_PER_PAGE;

        String recordsPerPageStr = request.getParameter("records_on_page");
        if (recordsPerPageStr != null) {
            recordPerPage = Integer.parseInt(recordsPerPageStr);
        }

        return recordPerPage;
    }

    private static int getCurrentPage(HttpServletRequest request) {
        int currentPage = DEFAULT_PAGE;
        String pageStr = request.getParameter("page");

        if (pageStr != null) {
            currentPage = Integer.parseInt(pageStr);
        }

        return currentPage;
    }
}
