package org.example.app.model;

import jakarta.servlet.http.HttpServletRequest;
import org.example.app.db_connect.DbConnectInit;
import org.example.app.entity.Employee;
import org.example.app.repository.EmployeeRepository;
import org.example.app.utils.ActionAnswer;


public class CreateEmployeeModel {
    private final Employee newEmployee;
    public CreateEmployeeModel(HttpServletRequest request) {
        this.newEmployee = new Employee(
                request.getParameter("firstName"),
                request.getParameter("lastName"),
                request.getParameter("email"),
                request.getParameter("phone"),
                request.getParameter("position"),
                Boolean.parseBoolean(request.getParameter("isWork"))
        );
    }

    public ActionAnswer<Employee> createEmployee (DbConnectInit connection) {

        EmployeeRepository userRepository = new EmployeeRepository(connection);
        return userRepository.create(this.newEmployee);
    }

}
