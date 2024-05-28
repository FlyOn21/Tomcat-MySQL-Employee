package org.example.app.model;

import org.example.app.db_connect.DbConnectInit;
import org.example.app.entity.Employee;
import org.example.app.repository.EmployeeRepository;
import org.example.app.utils.ActionAnswer;



public class DeleteEmployeeModel {


    public ActionAnswer<Employee> deleteEmployee(Long employeeId, DbConnectInit connection) {
        EmployeeRepository userRepository = new EmployeeRepository(connection);
        return userRepository.delete(employeeId);
    }
}
