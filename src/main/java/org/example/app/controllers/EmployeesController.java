package org.example.app.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.db_connect.DbConnectInit;
import org.example.app.db_connect.GetConnect;
import org.example.app.entity.Employee;
import org.example.app.model.CreateEmployeeModel;
import org.example.app.model.DeleteEmployeeModel;
import org.example.app.model.GetEmployeeModel;
import org.example.app.model.UpdateEmployeeModel;
import org.example.app.repository.EmployeeRepository;
import org.example.app.utils.ActionAnswer;
import org.example.app.utils.ListToString;
import org.example.app.utils.ReadParams;
import org.example.app.utils.validate.validate_entity.ValidateAnswer;
import org.example.app.validation.ValidationForm;

import java.io.IOException;
import java.sql.SQLException;

public class EmployeesController extends HttpServlet {
    private static final Logger PATH_LOGGER =
            LogManager.getLogger(EmployeeRepository.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");
    private final DbConnectInit connection = new GetConnect().connect();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        PATH_LOGGER.info("TEST ============== {}",action);
        try {
            switch (action) {
                case "/new" -> showNewForm(request, response);
                case "/insert" -> create(request, response);
                case "/delete" -> delete(request, response);
                case "/edit" -> showEditForm(request, response);
                case "/update" -> update(request, response);
                default -> read(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void create(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ValidationForm validationForm = new ValidationForm(request, connection, false);
        if (validationForm.isValidForm()) {

            ActionAnswer<Employee> createAnswer = new CreateEmployeeModel(request).createEmployee(connection);
            if (!createAnswer.isSuccess()) {
                request.setAttribute("errors", ListToString.listToString(createAnswer.getErrors()));
                showNewForm(request, response);
            } else {
                request.getSession().setAttribute("successMsg", "Employee was created successfully!");
                response.sendRedirect("/");
            }

        } else {
            request.setAttribute("validationFormErrors", validationForm.getValidationFormErrors());
            request.setAttribute("previousInput", validationForm.getPreviousData());
            PATH_LOGGER.info(String.format("Errors: %s", validationForm.getValidationFormErrors().toString()));
            showNewForm(request, response);
        }
    }

    private void read(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ReadParams readParams = new ReadParams();
        GetEmployeeModel getEmployeeModel = new GetEmployeeModel(readParams);
        ActionAnswer<Employee> readAnswer = getEmployeeModel.getEmployees(connection);
        if (readAnswer.isSuccess()) {
            String successMsg = (String) request.getSession().getAttribute("successMsg");
            request.setAttribute("successMsg", successMsg);
            request.setAttribute("listEmployee", readAnswer.getEntity());
            request.getSession().removeAttribute("successMsg");
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("pages/employee_list.jsp");
            dispatcher.forward(request, response);
        } else {
            throw new ServletException(ListToString.listToString(readAnswer.getErrors()));
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        ValidationForm validationForm = new ValidationForm(request, connection, true);
        if (validationForm.isValidForm()) {
            ReadParams checkUpdateEmployee = new ReadParams();
            CONSOLE_LOGGER.info("EmployeeId: {}", request.getParameter("id"));
            ValidateAnswer validate =checkUpdateEmployee.setEmployeeId(request.getParameter("id"));
            if (!validate.isValid()) {
                throw new ServletException(ListToString.listToString(validate.getErrorsList()));
            }
            ActionAnswer<Employee> updateAnswer = new UpdateEmployeeModel(checkUpdateEmployee, request).updateUser(connection);
            if (!updateAnswer.isSuccess()) {
                request.setAttribute("errors", ListToString.listToString(updateAnswer.getErrors()));
                showEditForm(request, response);
            }
            request.getSession().setAttribute("successMsg", "Employee was update successfully!");
            response.sendRedirect("/");
        } else {
            request.setAttribute("validationFormErrors", validationForm.getValidationFormErrors());
            showEditForm(request, response);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Long id =Long.parseLong(request.getParameter("id"));
        ActionAnswer<Employee> deleteEmployee = new DeleteEmployeeModel().deleteEmployee(id, connection);
        if (!deleteEmployee.isSuccess()) {
            throw new ServletException(ListToString.listToString(deleteEmployee.getErrors()));
        }
        request.getSession().setAttribute("successMsg", "Employee was deleted successfully!");
        response.sendRedirect("/");
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("pages/employee_create_form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ReadParams checkUpdateEmployee = new ReadParams();
        ValidateAnswer validate =checkUpdateEmployee.setEmployeeId(request.getParameter("id"));
        if (!validate.isValid()) {
            throw new ServletException(ListToString.listToString(validate.getErrorsList()));
        }
        ActionAnswer<Employee> toUpdateEmployee = new GetEmployeeModel(checkUpdateEmployee).getEmployee(connection);
        Employee employeeToUpdate = toUpdateEmployee.getEntity().getFirst();
        request.setAttribute("employee", employeeToUpdate);
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("pages/employee_update_form.jsp");
        dispatcher.forward(request, response);
    }
}
