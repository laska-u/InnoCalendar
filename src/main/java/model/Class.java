package model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Class {

    public Class() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course_id;

    private String teacher;
    private String room;

    public void setId(long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course_id;
    }

    public void setCourse(Course course) {
        this.course_id = course;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    private java.util.Date date;

    @Basic
    @Temporal(TemporalType.TIME)
    private java.util.Date time;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
