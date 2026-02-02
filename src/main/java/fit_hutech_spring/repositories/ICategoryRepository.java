package fit_hutech_spring.repositories;

import fit_hutech_spring.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends
        JpaRepository<Category, Long> {
}