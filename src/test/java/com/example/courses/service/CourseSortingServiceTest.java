package com.example.courses.service;

import com.example.courses.dto.CourseDTO;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.courses.service.CourseSortingService.*;
import static org.junit.jupiter.api.Assertions.*;

class CourseSortingServiceTest {
    CourseSortingService courseSortingService = new CourseSortingService();

    static CourseDTO first;
    static CourseDTO second;
    static CourseDTO third;

    static List<CourseDTO> courseDTOList;
    static List<CourseDTO> expected;

    @BeforeAll
    static void setUp(){
        first = getCourseDTO("Linux basics", 5, 5);
        second = getCourseDTO("Java", 25, 15);
        third = getCourseDTO("AWS", 10, 20);
        courseDTOList = Arrays.asList(first, second, third);
    }

    // test sorting by title
    @Test
    void testSortingByTitle() {
        // ascending order
        expected = List.of(third, second, first);
        courseSortingService.applySoring(courseDTOList, SORT_BY_TITLE, SORT_ORDER_ASCENDING);
        assertEquals(expected, courseDTOList);

        // descending order
        expected = List.of(first, second, third);
        courseSortingService.applySoring(courseDTOList, SORT_BY_TITLE, SORT_ORDER_DESCENDING);
        assertEquals(expected, courseDTOList);
    }

    // test sorting by duration
    @Test
    void testSortingByDuration() {
        // ascending order
        expected = List.of(first, third, second);
        courseSortingService.applySoring(courseDTOList, SORT_BY_DURATION, SORT_ORDER_ASCENDING);
        assertEquals(expected, courseDTOList);

        // descending order
        expected = List.of(second, third, first);
        courseSortingService.applySoring(courseDTOList, SORT_BY_DURATION, SORT_ORDER_DESCENDING);
        assertEquals(expected, courseDTOList);
    }

    // test sorting by number of students
    @Test
    void testSortingByNumberOfStudents() {
        // ascending order
        expected = List.of(first, second, third);
        courseSortingService.applySoring(courseDTOList, SORT_BY_NUMBER_OF_STUDENTS, SORT_ORDER_ASCENDING);
        assertEquals(expected, courseDTOList);

        // descending order
        expected = List.of(third, second, first);
        courseSortingService.applySoring(courseDTOList, SORT_BY_NUMBER_OF_STUDENTS, SORT_ORDER_DESCENDING);
        assertEquals(expected, courseDTOList);
    }

    @Test
    void testGetSortingOptions(){
        Map<Integer, String> sortingOptions = courseSortingService.getSortingOptions("en");
        Map<Integer, String> expected = Map.of(
                    SORT_BY_TITLE, "title",
                    SORT_BY_DURATION, "duration",
                    SORT_BY_NUMBER_OF_STUDENTS, "number of students"
                );

        assertEquals(sortingOptions, expected);
    }

    @Test
    void testGetSortingOrderOptions(){
        Map<Integer, String> sortingOrderOptions = courseSortingService.getSortingOrderOptions("en");
        Map<Integer, String> expected = Map.of(
                    SORT_ORDER_ASCENDING, "ascending",
                    SORT_ORDER_DESCENDING, "descending"
                );

        assertEquals(sortingOrderOptions, expected);
    }

    static CourseDTO getCourseDTO(String title, int durationInDays, int numberOfStudents){
        CourseDTO courseDTO = new CourseDTO();

        Course course = new Course.Builder()
                .setTitle(title)
                .setStartDate(LocalDateTime.now())
                .setEndDate(LocalDateTime.now().plusDays(durationInDays))
                .build();

        List<User> studentsList = new ArrayList<>();
        for(int i = 0; i < numberOfStudents; i++){
            studentsList.add(new User());
        }

        courseDTO.setCourse(course);
        courseDTO.setStudents(studentsList);

        return courseDTO;
    }
}