package yusuf.EmployeeDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import yusuf.DButil.DButil;
import yusuf.model.Employee;

public class EmployeeDao {

    public List<Employee> getAllEmployee() {
        Connection connection = DButil.getConnection();
        List<Employee> listEmployees = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from employee");

            while (resultSet.next()) {
                Employee employee = new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4));
                listEmployees.add(employee);
            }

        } catch (SQLException e) {
            System.out.println("Failed to create connection: " + e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return listEmployees;
    }

    public void addEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection
                    .prepareStatement("insert into employee(name,lastname,salary) values (?,?,?)");
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setInt(3, employee.getSalary());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to create preparedstatement: " + e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void updateEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection
                    .prepareStatement("update employee set name=?,lastname=?,salary=? where id=?");
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getLastname());
            preparedStatement.setInt(3, employee.getSalary());
            preparedStatement.setInt(4, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public void deleteEmployee(Employee employee) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("delete from employee where id=?");
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Employee getEmployee(int id) {
        Connection connection = DButil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        try {
            preparedStatement = connection
                    .prepareStatement("select * from employee where id=?");
                    preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employee = new Employee(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DButil.closeConnection(connection);
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        return employee;

    }


}
