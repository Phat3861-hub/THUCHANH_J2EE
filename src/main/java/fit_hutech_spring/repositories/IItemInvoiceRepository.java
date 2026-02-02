package fit_hutech_spring.repositories;

import fit_hutech_spring.entities.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemInvoiceRepository extends
        JpaRepository<ItemInvoice, Long> {
}