package com.hibernateems;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Failed to update employee. Error: " + e.getMessage());
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
            employees = session.createQuery("FROM Employee " +
                            "WHERE id <> :id AND (name LIKE :search OR age = :age OR address LIKE :search OR salary = :salary)", Employee.class)
                    .setParameter("id", 0)
                    .setParameter("search", "%" + searchCriteria + "%")
                    .setParameter("age", parseInteger(searchCriteria))
                    .setParameter("salary", parseDouble(searchCriteria))
                    .list();
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
            employee = session.createQuery("FROM Employee WHERE name = :name", Employee.class)
                    .setParameter("name", name)
                    .uniqueResult();
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
