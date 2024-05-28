package org.example.app.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.app.db_connect.interfaces.IConnection;
import org.example.app.entity.Employee;
import org.example.app.repository.interfaces.IRepository;
import org.example.app.utils.ActionAnswer;
import org.example.app.utils.logging.LoggingErrorsMsg;
import org.example.app.utils.logging.LoggingSuccessMsg;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EmployeeRepository implements IRepository<Employee> {
    private final IConnection connection;
    private static final Logger CRUD_LOGGER =
            LogManager.getLogger(EmployeeRepository.class);
    private static final Logger CONSOLE_LOGGER =
            LogManager.getLogger("console_logger");

    public EmployeeRepository(IConnection connection) {
        this.connection = connection;
    }

    @Override
    public ActionAnswer<Employee> create(Employee obj) {
        Transaction transaction = null;
        ActionAnswer<Employee> actionAnswer = new ActionAnswer<>();
        boolean isEmailExists = checkEmailExists(obj.getEmail());
        if (isEmailExists) {
            actionAnswer.addError("Email already exists");
            return actionAnswer;
        }
        try(Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(obj);
            transaction.commit();
            actionAnswer.setIsSuccess();
            CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_ADDED.getMsg(), "Entity Employee" ));
            actionAnswer.setMsg("Employee created successfully");
            actionAnswer.setEntity(Collections.singletonList(obj));
            return actionAnswer;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            actionAnswer.setMsg("Create error");
            actionAnswer.addError(LoggingErrorsMsg.DB_INSERT_ERROR.getMsg());
            return actionAnswer;
        }
    }

    public boolean checkEmailExists(String emailValue) {
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
            Root<Employee> root = cr.from(Employee.class);
            cr.select(root).where(cb.equal(root.get("email"), emailValue)).distinct(true);
            long resultCount = session.createQuery(cr).stream().count();
            transaction.commit();

            return resultCount > 0;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            throw new HibernateException(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
        }
    }

    @Override
    public ActionAnswer<Employee> readAll(int limit, int offset) {
        Transaction transaction = null;
        ActionAnswer<Employee> actionAnswer = new ActionAnswer<>();
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            cq.from(Employee.class);
            Query<Employee> query = session.createQuery(cq);
            if (limit > 0) {
                query.setMaxResults(limit);
            }
            if (offset > 0) {
                query.setFirstResult(offset);
            }
            List<Employee> result = query.getResultList();
            transaction.commit();
            actionAnswer.setIsSuccess();
            if (result.isEmpty()) {
                actionAnswer.setMsg("No Employees found");
                return actionAnswer;
            }
            actionAnswer.setMsg("Employees found");
            actionAnswer.setEntity(result);
            return actionAnswer;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            actionAnswer.setMsg("Get employee error");
            actionAnswer.addError(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
            CRUD_LOGGER.error(e.getMessage(), e);
            return actionAnswer;
        }
    }

    @Override
    public ActionAnswer<Employee> update(Employee obj) {
        ActionAnswer<Employee> actionAnswer = new ActionAnswer<>();
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(obj);
            transaction.commit();
            actionAnswer.setIsSuccess();
            CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_UPDATED.getMsg(), "Entity Employee" ));
            actionAnswer.setMsg("Employee update successfully");
            actionAnswer.setEntity(Collections.singletonList(obj));
            return actionAnswer;
        } catch (HeadlessException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            actionAnswer.setMsg("Update error");
            actionAnswer.addError(LoggingErrorsMsg.DB_UPDATE_ERROR.getMsg());
            return actionAnswer;
        }
    }

    @Override
    public ActionAnswer<Employee> delete(Long id) {
        ActionAnswer<Employee> actionAnswer = new ActionAnswer<>();
        Transaction transaction = null;
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
                transaction.commit();
                actionAnswer.setIsSuccess();
                CONSOLE_LOGGER.info(String.format("%s %s%n",LoggingSuccessMsg.DB_ENTITY_DELETED.getMsg(), "Entity Employee" ));
                actionAnswer.setMsg("User deleted successfully");
                actionAnswer.setEntity(Collections.singletonList(employee));
            } else {
                actionAnswer.setMsg("Employee not found");
            }
            return actionAnswer;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            actionAnswer.setMsg("Delete error");
            actionAnswer.addError(LoggingErrorsMsg.DB_DELETE_ERROR.getMsg());
            return actionAnswer;
        }
    }

    @Override
    public ActionAnswer<Employee> readById(Long employeeId) {
        Transaction transaction = null;
        ActionAnswer<Employee> actionAnswer = new ActionAnswer<>();
        try (Session session = this.connection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> root = cq.from(Employee.class);

            cq.where(cb.equal(root.get("id"), employeeId));
            List <Employee> result = session.createQuery(cq).getResultList();
            transaction.commit();
            if (result.isEmpty()) {
                actionAnswer.setMsg("No employee found");
                return actionAnswer;
            }
            actionAnswer.setIsSuccess();
            actionAnswer.setMsg("Employee found");
            actionAnswer.setEntity(result);
            return actionAnswer;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            CRUD_LOGGER.error(e.getMessage(), e);
            actionAnswer.setMsg("Get employee error");
            actionAnswer.addError(LoggingErrorsMsg.DB_QUERY_ERROR.getMsg());
            return actionAnswer;

        }
    }

}
