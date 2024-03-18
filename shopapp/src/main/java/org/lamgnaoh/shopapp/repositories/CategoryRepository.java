package org.lamgnaoh.shopapp.repositories;

import org.lamgnaoh.shopapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
