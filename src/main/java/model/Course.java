package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "\"Courses\"")
public class Course {

    @Id
    private long id;

    private String name;

    public Course(Course course) {
    }

    @ManyToMany(mappedBy = "courses")
    private Set<User> users = new HashSet<>();

    public void setId(long id) {
        this.id = id;
    }

    public Course() {
    }

    public Course(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
