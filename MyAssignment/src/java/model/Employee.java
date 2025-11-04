package model;

public class Employee extends BaseModel {
    private String name;
    private Department dept;      // map với Division.did
    private Employee supervisor;  // self-FK supervisorid
    /**
     * Trạng thái làm việc (tuỳ bạn dùng cột trong DB):
     * 0 = Probation (Thử việc), 1 = Official (Chính thức), 2 = Terminated (Nghỉ việc)
     * Nếu DB chưa có cột, bạn có thể bỏ field này.
     */
    private Integer employmentStatus;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Department getDept() { return dept; }
    public void setDept(Department dept) { this.dept = dept; }

    public Employee getSupervisor() { return supervisor; }
    public void setSupervisor(Employee supervisor) { this.supervisor = supervisor; }

    public Integer getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(Integer employmentStatus) { this.employmentStatus = employmentStatus; }
}
