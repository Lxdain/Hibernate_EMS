# Hibernate_EMS
Remake of the JDBC Employee Management System utilizing Hibernate. Alongside Hibernate I am exploring Maven as a dependancy manager.

## In Detail
Another project I made as part of the ITAcademy curriculum, quite similar to the [JDBC_EMS](https://github.com/lxdain/JDBC_EMS/) project I made with the diference being the use of HibernateORM.

This time, you can clearly see the use of annotations (which I quite prefer over writing SQL queries), also Maven was used as a dependancy manager for the project so it was easier to get started with actual coding.
```java
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
```
As you can see we have successfully eliminated the need to write ugly, bulky and repetative SQL queries! :D
