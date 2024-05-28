package org.example.app.model;

import org.example.app.db_connect.DbConnectInit;
import org.example.app.entity.Employee;
import org.example.app.repository.EmployeeRepository;
import org.example.app.utils.ActionAnswer;
import org.example.app.utils.ReadParams;


public class GetEmployeeModel {
    private final int limit;
    private final int offset;
    private final Long id;

    public GetEmployeeModel(ReadParams readParams) {
        this.limit = readParams.getLimit();
        this.offset = readParams.getOffset();
        this.id = readParams.getId();
    }


    public ActionAnswer<Employee> getEmployees(DbConnectInit connection) {
        EmployeeRepository userRepository = new EmployeeRepository(connection);
        return userRepository.readAll(this.limit, this.offset);
    }

    public ActionAnswer<Employee> getEmployee(DbConnectInit connection) {
        EmployeeRepository userRepository = new EmployeeRepository(connection);
        return userRepository.readById(this.id);
    }

}
