package com.showdown.wildfly.userservice.Repository;

import com.showdown.wildfly.userservice.Models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "userPU")
    private EntityManager em;

    public void addUser(User user) {
        em.persist(user);
    }

    public User getUserById(String id) {
        return em.find(User.class, id);
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    public User updateUser(User user) {
        return em.merge(user);
    }

    public void deleteUser(String id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }
    public User login(String username, String password) {

        System.out.println("Repository username = " + username);
        System.out.println("Repository password = " + password);

        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                    User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public User findByEmail(String email) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email",
                    User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username",
                    User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}