package org.hibernatejpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernatejpa.entity.Student;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
        //my-persistence-unit we are giving only name of this persistence unit.
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Student student = new Student();
        student.setName("Ankesh");
        student.setStudentFees(1000);

        entityManager.persist(student);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
