package ru.ivanov.authenticationservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.authenticationservice.data.model.AdminCredential;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface AdminCredentialRepository extends JpaRepository<AdminCredential, UUID> {

    Optional<AdminCredential> findByLogin(String login);

}
