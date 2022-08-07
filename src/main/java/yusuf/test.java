package yusuf;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import yusuf.DButil.DButil;
import yusuf.EmployeeController.EmployeeController;
import yusuf.EmployeeDao.EmployeeDao;
import yusuf.model.Employee;

public class test {
    public static void main(String[] args) {
        EmployeeController employeeController = new EmployeeController();
        // Veri ekleme
         Employee employee = new Employee("Yusuf", "Dagdeviren", 80000);
         Employee employee2 = new Employee("Zehra", "Cakir", 100000);
         employeeController.addEmployee(employee);
         employeeController.addEmployee(employee2);
        // Bütün verileri çekme
         List<Employee> list = new ArrayList<>();
         list = employeeController.getAllEmployee();
         for(Employee e:list){
             System.out.println("Adi: "+e.getName()+" Soyadi: "+e.getLastname()+" Maasi: "+e.getSalary());
         }
        // Id ye göre Veri çekme
         int id = 6;
         Employee e = employeeController.getEmployee(id);
         System.out.println("Adi: "+e.getName()+" Soyadi: "+e.getLastname()+" Maasi: "+e.getSalary());
        // Veri Silme
         Employee employee1 = new Employee(5,"Yusuf", "Dagdeviren", 80000);
         employeeController.deleteEmployee(employee1);
        // Veri updateleme
        Employee employee3 = new Employee(6,"Zehra", "Cakir", 134567);
        employeeController.updateEmployee(employee3);
    }
}
