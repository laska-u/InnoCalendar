package model;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class Course {

    private long id;
    private String name;
    private List<User> users;
    private List<Class> classes;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }
}
