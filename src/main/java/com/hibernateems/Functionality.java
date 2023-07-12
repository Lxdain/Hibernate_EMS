package com.hibernateems;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class Functionality {
    private SessionFactory sessionFactory;
    private int nextEmployeeId;

    public Functionality() {
        createSessionFactory();
        getNextEmployeeIdFromDatabase();
    }

    private void createSessionFactory() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    private void getNextEmployeeIdFromDatabase() {
        try (Session session = sessionFactory.openSession()) {
            Long maxId = session.createQuery("SELECT MAX(id) FROM Employee", Long.class).uniqueResult();
            nextEmployeeId = maxId != null ? maxId.intValue() + 1 : 1;
        } catch (Exception e) {
            System.out.println("Error occurred while retrieving the next employee ID: " + e.getMessage());
        }
    }

    public int getNextEmployeeId() {
        return nextEmployeeId;
    }

    public void addEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(employee);
            transaction.commit();
            nextEmployeeId++;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Failed to add employee. Error: " + e.getMessage());
        }
    }

    public void editEmployee(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(employee);
                transaction.commit();
            } catch (Exception ex) {
                transaction.rollback();
                throw ex;
            }
        }
    }

    public void deleteEmployee(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                transaction.commit();
            } else {
                System.out.println("Employee with ID " + id + " not found.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Failed to delete employee. Error: " + e.getMessage());
        }
    }

    public List<Employee> retrieveEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            employees = session.createQuery("FROM Employee", Employee.class).list();
        } catch (Exception e) {
            System.out.println("Failed to retrieve employees. Error: " + e.getMessage());
        }
        return employees;
    }

    public List<Employee> searchEmployees(String searchCriteria) {
        List<Employee> employees = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);
            criteriaQuery.select(root).where(
                    criteriaBuilder.notEqual(root.get("id"), 0),
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("name"), "%" + searchCriteria + "%"),
                            criteriaBuilder.equal(root.get("age"), parseInteger(searchCriteria)),
                            criteriaBuilder.like(root.get("address"), "%" + searchCriteria + "%"),
                            criteriaBuilder.equal(root.get("salary"), parseDouble(searchCriteria))
                    )
            );
            employees = session.createQuery(criteriaQuery).list();
        } catch (Exception e) {
            System.out.println("Failed to search employees. Error: " + e.getMessage());
        }
        return employees;
    }

    public Employee getEmployeeById(int id) {
        Employee employee = null;
        try (Session session = sessionFactory.openSession()) {
            employee = session.get(Employee.class, id);
        } catch (Exception e) {
            System.out.println("Failed to retrieve employee. Error: " + e.getMessage());
        }
        return employee;
    }

    public Employee getEmployeeByName(String name) {
        Employee employee = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            employee = session.createQuery(criteriaQuery).uniqueResult();
        } catch (Exception e) {
            System.out.println("Failed to retrieve employee. Error: " + e.getMessage());
        }
        return employee;
    }

    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}




