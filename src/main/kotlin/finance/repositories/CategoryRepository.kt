package finance.repositories

import finance.models.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository<T : Category> : JpaRepository<T, Long> {

}