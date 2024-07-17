package ru.ivanov.authenticationservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.authenticationservice.data.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @Query(value = """
            SELECT rt
            FROM RefreshToken rt
            WHERE rt.adminCredential.login=:login
            """)
    Optional<RefreshToken> findByAdminLogin(String login);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM RefreshToken r
            WHERE r.refreshToken=:refreshToken
            """)
    void deleteByRefreshToken(String refreshToken);

}
