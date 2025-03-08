CREATE DATABASE StudentAttendanceSystems;
USE StudentAttendanceSystems;

CREATE TABLE StudentDetailss (
    Student_ID VARCHAR(50),
    Student_Name VARCHAR(50),
    Student_Department VARCHAR(50),
    Total_Classes INT NOT NULL DEFAULT 0,
    Attended_Classes INT NOT NULL DEFAULT 0,
    Remaining_Classes INT AS (Total_Classes - Attended_Classes) STORED,
    Attendance_Marks DECIMAL(5,2) NOT NULL DEFAULT 0
);

DESC StudentDetailss;
