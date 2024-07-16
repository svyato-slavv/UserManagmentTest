package ru.ivanov.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.userservice.exception.NotFoundException;
import ru.ivanov.userservice.model.User;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(int id);

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    default void deleteExistedUser(int id) {
        if (delete(id) == 0) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
    }

    default User getExistedUser(int id) {
        return findById(id).orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
    }

}

