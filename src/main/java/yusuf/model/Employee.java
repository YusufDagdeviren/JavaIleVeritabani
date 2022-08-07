package yusuf.model;

public class Employee {
    private int id;
    private String name;
    private String lastname;
    private int salary;

    public Employee() {
    }

    public Employee(String name, String lastname, int salary) {
        this.name = name;
        this.lastname = lastname;
        this.salary = salary;
    }
    public Employee(int id,String name, String lastname, int salary) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.salary = salary;
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

}
