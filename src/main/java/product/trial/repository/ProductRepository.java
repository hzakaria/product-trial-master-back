package product.trial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product.trial.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}