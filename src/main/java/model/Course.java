package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "user_id")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "course_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Class> classes = new HashSet<Class>();

    public void setId(long id) {
        this.id = id;
    }

    public Course() {

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Class> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class> classes) {
        this.classes = classes;
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
