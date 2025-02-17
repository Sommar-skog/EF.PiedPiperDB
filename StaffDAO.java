package com.example.piedpiperdb.DAO;

import com.example.piedpiperdb.Entities.Staff;
import jakarta.persistence.*;
import java.util.List;
import java.util.logging.Logger;
import com.example.piedpiperdb.Entities.Game;
import java.util.ArrayList;
import com.example.piedpiperdb.Entities.Player;

//GEFP-45 Anna R
public class StaffDAO {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("piedpiperdb");

    //lägger till ny personal
    public boolean savePersonal(Staff staff) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(staff);
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error");
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        } finally {
            entityManager.close();
        }
    }
    //Hämta en lista över personal
    public List<Staff> getAllStaff() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        List<Staff> staff = null;

        try {
            TypedQuery<Staff> query = entityManager.createQuery("FROM Staff", Staff.class);
            staff = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            entityManager.close();
        }
        return staff;
    }
    //Kontrollera behörighet
    public boolean harPermissionToStaff(String staffName) {
        return "admin".equalsIgnoreCase(staffName);
    }
    private com.example.piedpiperdb.DAO.TeamDAO teamDAO = new com.example.piedpiperdb.DAO.TeamDAO();

    //uppdatera personal
    public boolean updatePersonal (int staffId, String firstName, String lastName, String nickname, String streetAdress, String city, String postalCode, String country, String email) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Staff staff = entityManager.find(Staff.class, staffId);
            if (staff != null) {
                staff.setFirstName(firstName);
                staff.setLastName(lastName);
                staff.setNickname(nickname);
                staff.setStreetAdress(streetAdress);
                staff.setCity(city);
                staff.setCountry(country);
                staff.setPostalCode(postalCode);
                staff.setEmail(email);
                entityManager.merge(staff);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error");
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        } finally {
            entityManager.close();
        }
    }
    //ta bort personal
    public boolean deletePersonal(int staffId) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Staff staff = entityManager.find(Staff.class, staffId);
            if (staff != null) {
                entityManager.remove(staff);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error");
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        } finally {
            entityManager.close();
        }
    }
}
