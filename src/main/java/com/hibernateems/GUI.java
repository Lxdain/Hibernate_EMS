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
        setTitle("Employee Management System");
        setContentPane(accountPanel);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        functionality = new Functionality();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String ageText = ageField.getText();
                String address = addressField.getText();
                String salaryText = salaryField.getText();

                if (!isValidNumber(ageText) || !isValidNumber(salaryText)) {
                    outputLabel.setText("Invalid input type. These fields only accept numeric parameters.");
                    clearOutputLabelAfterDelay(3000);
                    return;
                }

                int age = Integer.parseInt(ageText);
                double salary = Double.parseDouble(salaryText);

                Employee employee = new Employee(name, age, address, salary);
                functionality.addEmployee(employee);
                displayEmployees();
                clearFields();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedEmployee != null) {
                    functionality.deleteEmployee(selectedEmployee.getId());
                    displayEmployees();
                    clearFields();
                    outputLabel.setText("Employee Successfully Deleted!");

                    // Delayed clearing of outputLabel after 3 seconds
                    Timer timer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            outputLabel.setText("");
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchCriteria = textField1.getText();
                List<Employee> employees = functionality.searchEmployees(searchCriteria);
                displayEmployees(employees);
                clearFields();
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Employee> employees = functionality.retrieveEmployees();
                displayEmployees(employees);
                clearFields();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedEmployee != null) {
                    String name = nameField.getText();
                    String ageText = ageField.getText();
                    String address = addressField.getText();
                    String salaryText = salaryField.getText();

                    if (!isValidNumber(ageText) || !isValidNumber(salaryText)) {
                        outputLabel.setText("Invalid input type. These fields only accept numeric parameters.");
                        clearOutputLabelAfterDelay(3000);
                        return;
                    }

                    int age = Integer.parseInt(ageText);
                    double salary = Double.parseDouble(salaryText);

                    selectedEmployee.setName(name);
                    selectedEmployee.setAge(age);
                    selectedEmployee.setAddress(address);
                    selectedEmployee.setSalary(salary);
                    functionality.editEmployee(selectedEmployee);
                    outputLabel.setText("Employee edited successfully!");
                    clearFields();
                }
            }
        });

        outputComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEmployeeName = (String) outputComboBox.getSelectedItem();
                if (selectedEmployeeName != null) {
                    Employee employee = functionality.getEmployeeByName(selectedEmployeeName);
                    if (employee != null) {
                        selectedEmployee = employee;
                        setEmployeeFields(employee); // Populate the fields with the selected employee info
                        displayEmployeeInfo(employee);
                    }
                }
            }
        });
    }

    private boolean isValidNumber(String text) {
        return text.matches("\\d+");
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

    private void displayEmployees() {
        List<Employee> employees = functionality.retrieveEmployees();
        outputComboBox.removeAllItems(); // Clear previous items
        for (Employee employee : employees) {
            outputComboBox.addItem(employee.getName()); // Add employee name to JComboBox
        }
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
        textField1.setText("");
        selectedEmployee = null;
    }

    private void setEmployeeFields(Employee employee) {
        nameField.setText(employee.getName());
        ageField.setText(String.valueOf(employee.getAge()));
        addressField.setText(employee.getAddress());
        salaryField.setText(String.valueOf(employee.getSalary()));
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

