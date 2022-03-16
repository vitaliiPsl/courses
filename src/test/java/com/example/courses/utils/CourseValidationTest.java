package com.example.courses.utils;

import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.Role;
import com.example.courses.persistence.entity.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseValidationTest {
    @ParameterizedTest
    @NullSource
    void testCourseNull(Course course){
        assertFalse(CourseValidation.isCourseValid(course));
    }

    @ParameterizedTest
    @MethodSource("makeInvalidTitles")
    void testInvalidTitle(String title){
        assertFalse(CourseValidation.isTitleValid(title));
    }

    @ParameterizedTest
    @MethodSource("makeValidTitles")
    void testValidTitle(String title){
        assertTrue(CourseValidation.isTitleValid(title));
    }

    @ParameterizedTest
    @MethodSource("makeInvalidDates")
    void testInvalidDates(LocalDateTime startDate, LocalDateTime endDate){
        assertFalse(CourseValidation.isStartAndEndDateValid(startDate, endDate));
    }

    @ParameterizedTest
    @MethodSource("makeValidDates")
    void testValidDates(LocalDateTime startDate, LocalDateTime endDate){
        assertTrue(CourseValidation.isStartAndEndDateValid(startDate, endDate));
    }

    @ParameterizedTest
    @MethodSource("makeInvalidCourse")
    void testInvalidCourse(Course course){
        assertFalse(CourseValidation.isCourseValid(course));
    }

    @ParameterizedTest
    @MethodSource("makeValidCourse")
    void testValidCourse(Course course){
        assertTrue(CourseValidation.isCourseValid(course));
    }


    static List<Course> makeValidCourse(){
        return List.of(
                new Course.Builder()
                        .setTitle("Java")
                        .setStartDate(LocalDateTime.now().plusHours(1))
                        .setEndDate(LocalDateTime.now().plusHours(5))
                        .build(),
                new Course.Builder()
                        .setTitle(" Java ")
                        .setStartDate(LocalDateTime.now().plusDays(1))
                        .setEndDate(LocalDateTime.now().plusHours(25))
                        .build()
        );
    }

    static List<Course> makeInvalidCourse(){
        return List.of(
                new Course.Builder()
                        .setTitle(null)
                        .setStartDate(null)
                        .setEndDate(null).build(),
                new Course.Builder()
                        .setTitle("  ")
                        .setStartDate(LocalDateTime.now())
                        .setEndDate(LocalDateTime.now().plusDays(1))
                        .build(),
                new Course.Builder()
                        .setTitle("")
                        .setStartDate(LocalDateTime.now().plusDays(1))
                        .setEndDate(LocalDateTime.now())
                        .build()
        );
    }

    static List<String> makeInvalidTitles(){
        return Arrays.asList(null, "", "   ");
    }

    static List<String> makeValidTitles(){
        return Arrays.asList("Programming", " Programming ", "Програмування ");
    }

    static List<Arguments> makeInvalidDates(){
        return List.of(
                Arguments.arguments(null, null),
                Arguments.arguments(LocalDateTime.now(), null),
                Arguments.arguments(null, LocalDateTime.now()),
                Arguments.arguments(LocalDateTime.now(), LocalDateTime.now()),
                Arguments.arguments(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5)),
                Arguments.arguments(LocalDateTime.of(2022, 3, 10, 10, 10), LocalDateTime.of(2022, 3, 1, 10, 10))
        );
    }
    static List<Arguments> makeValidDates(){
        return List.of(
                Arguments.arguments(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1)),
                Arguments.arguments(LocalDateTime.now().plusDays(5), LocalDateTime.now().plusMonths(3))
        );
    }
}