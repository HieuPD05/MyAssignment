package model;

import java.io.Serializable;
import java.util.List;

public class Employee implements Serializable {
    private String name;
    private String gender;
    private String dob;
    private List<String> skills;

    public Employee(String name, String gender, String dob, List<String> skills) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.skills = skills;
    }

    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
    public List<String> getSkills() { return skills; }
}
