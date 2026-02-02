package fit_hutech_spring.repositories;

import fit_hutech_spring.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    // Sửa từ User -> Optional<User>
    Optional<User> findByUsername(String username);
}