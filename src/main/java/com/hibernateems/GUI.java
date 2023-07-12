package com.hibernateems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JFrame {
    private JPanel accountPanel;
    private JLabel titleText;
    private JTextField nameField;
    private JLabel nameLabel;
    private JTextField ageField;
    private JLabel ageLabel;
    private JLabel addressLabel;
    private JTextField addressField;
    private JLabel salaryLabel;
    private JTextField salaryField;
    private JButton addButton;
    private JTextField textField1;
    private JComboBox<String> outputComboBox;
    private JLabel outputLabel;
    private JButton searchButton;
    private JButton showAllButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JLabel headerIcon;
    private Functionality functionality;
    private Employee selectedEmployee; // Store selected employee

    public GUI() {
        initialize();
        functionality = new Functionality();
        populateOutputComboBox();
        addOutputComboBoxListener();
        addButtonListener();
        searchButtonListener();
        showAllButtonListener();
        addSaveButtonListener();
        addDeleteButtonListener();
    }

    private void initialize() {
        setTitle("Employee Management System");
        setContentPane(accountPanel);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
    }

    private void populateOutputComboBox() {
        outputComboBox.removeAllItems();
        List<Employee> employees = functionality.retrieveEmployees();
        for (Employee employee : employees) {
            outputComboBox.addItem(employee.getName());
        }
    }

    private void addOutputComboBoxListener() {
        outputComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = (String) outputComboBox.getSelectedItem();
                if (selectedName != null) {
                    Employee selectedEmployee = functionality.getEmployeeByName(selectedName);
                    if (selectedEmployee != null) {
                        nameField.setText(selectedEmployee.getName());
                        ageField.setText(String.valueOf(selectedEmployee.getAge()));
                        addressField.setText(selectedEmployee.getAddress());
                        salaryField.setText(String.valueOf(selectedEmployee.getSalary()));
                    }
                }
            }
        });
    }

    private void addButtonListener() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String address = addressField.getText();
                double salary = Double.parseDouble(salaryField.getText());

                Employee employee = new Employee(name, age, address, salary);
                functionality.addEmployee(employee);
                populateOutputComboBox();
                outputLabel.setText("Employee added successfully!");
                displayEmployeeInfo(employee);
                clearFields();
                clearOutputLabelAfterDelay(3000);
            }
        });
    }

    private void searchButtonListener() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchCriteria = textField1.getText();
                List<Employee> employees = functionality.searchEmployees(searchCriteria);
                displayEmployees(employees);
                clearFields();
            }
        });
    }

    private void showAllButtonListener() {
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Employee> employees = functionality.retrieveEmployees();
                displayEmployees(employees);
                clearFields();
            }
        });
    }

    private void addSaveButtonListener() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (outputComboBox.getSelectedItem() != null) {
                    String selectedName = (String) outputComboBox.getSelectedItem();
                    Employee selectedEmployee = functionality.getEmployeeByName(selectedName);
                    if (selectedEmployee != null) {
                        String name = nameField.getText();
                        int age = Integer.parseInt(ageField.getText());
                        String address = addressField.getText();
                        double salary = Double.parseDouble(salaryField.getText());

                        selectedEmployee.setName(name);
                        selectedEmployee.setAge(age);
                        selectedEmployee.setAddress(address);
                        selectedEmployee.setSalary(salary);

                        functionality.editEmployee(selectedEmployee);
                        outputLabel.setText("Employee edited successfully!");
                        displayEmployeeInfo(selectedEmployee);
                        clearFields();
                        clearOutputLabelAfterDelay(3000);
                    }
                }
            }
        });
    }

    private void addDeleteButtonListener() {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (outputComboBox.getSelectedItem() != null) {
                    String selectedName = (String) outputComboBox.getSelectedItem();
                    Employee selectedEmployee = functionality.getEmployeeByName(selectedName);
                    if (selectedEmployee != null) {
                        functionality.deleteEmployee(selectedEmployee.getId());
                        outputLabel.setText("Employee deleted successfully!");
                        populateOutputComboBox();
                        clearFields();
                        clearOutputLabelAfterDelay(3000);
                    }
                }
            }
        });
    }

    private void displayEmployees(List<Employee> employees) {
        outputComboBox.removeAllItems(); // Clear previous items
        for (Employee employee : employees) {
            outputComboBox.addItem(employee.getName()); // Add employee name to JComboBox
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        addressField.setText("");
        salaryField.setText("");
    }

    private void clearOutputLabelAfterDelay(int delay) {
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void displayEmployeeInfo(Employee employee) {
        String info = "<html><b>ID:</b> " + employee.getId() + "<br>"
                + "<b>Name:</b> " + employee.getName() + "<br>"
                + "<b>Age:</b> " + employee.getAge() + "<br>"
                + "<b>Address:</b> " + employee.getAddress() + "<br>"
                + "<b>Salary:</b> " + employee.getSalary() + "</html>";
        outputLabel.setText(info);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}





