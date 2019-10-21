package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    private long id;

    private long chat_id;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "user_course",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    Set<Course> courses = new HashSet<>();

    public User() {
    }

    public User(long id, long chat_id) {
        this.id = id;
        this.chat_id = chat_id;
        courses = new HashSet<>();
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "model.User{" +
                "id=" + id +
                '}';
    }
}