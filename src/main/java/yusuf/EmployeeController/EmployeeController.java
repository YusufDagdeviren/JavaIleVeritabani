package yusuf.EmployeeController;

import java.util.List;

import yusuf.EmployeeDao.EmployeeDao;
import yusuf.model.Employee;

public class EmployeeController {
    private EmployeeDao employeeDao;

    public EmployeeController() {
        employeeDao = new EmployeeDao();
    }
    public List<Employee> getAllEmployee(){
        return employeeDao.getAllEmployee();
    }
    public void addEmployee(Employee employee){
        employeeDao.addEmployee(employee);
    }
    public void updateEmployee(Employee employee){
        employeeDao.updateEmployee(employee);
    }
    public void deleteEmployee(Employee employee){
        employeeDao.deleteEmployee(employee);
    }
    public Employee getEmployee(int id){
        return employeeDao.getEmployee(id);
    }

}
