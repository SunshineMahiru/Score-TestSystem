package com.example.model;

import com.example.util.MD5Util;

public class Score {
    private String studentId;
    private String studentName;
    private String course;
    private double score;

    public Score() {}

    public Score(String studentId, String studentName, String course, double score) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.course = course;
        this.score = score;
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
