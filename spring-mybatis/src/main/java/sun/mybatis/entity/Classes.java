package sun.mybatis.entity;

import java.util.List;

/**
 * Created by 264929 on 2015/6/15.
 */
public class Classes {
    private int id;
    private String name;
    /*
        teacher to class relationship is one-to-one mapping.
     */
    private Teacher teacher;

    private List<Student> students;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

}
