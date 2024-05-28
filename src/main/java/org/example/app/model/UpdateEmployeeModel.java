package org.example.app.model;

import jakarta.servlet.http.HttpServletRequest;
import org.example.app.db_connect.DbConnectInit;
import org.example.app.entity.Employee;
import org.example.app.repository.EmployeeRepository;
import org.example.app.utils.ActionAnswer;
import org.example.app.utils.ReadParams;

import java.util.UUID;

public class UpdateEmployeeModel {
    private final Employee user;

    public UpdateEmployeeModel(ReadParams checkUpdateEmployee, HttpServletRequest request) {
        this.user = new Employee(
                checkUpdateEmployee.getId(),
                UUID.fromString(request.getParameter("employeeId")),
                request.getParameter("firstName"),
                request.getParameter("lastName"),
                request.getParameter("email"),
                request.getParameter("phone"),
                request.getParameter("position"),
                Boolean.parseBoolean(request.getParameter("isWork"))
        );
    }

    public ActionAnswer<Employee> updateUser(DbConnectInit connection) {
        EmployeeRepository userRepository = new EmployeeRepository(connection);
        return userRepository.update(this.user);
    }

}
