package finance.models

import javax.persistence.*
import javax.validation.constraints.Size

@Entity(name = "CategoryEntity")
@Table(name = "t_categories")
class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var categoryId: Long = 0L
    @Size(max = 50)
    var category: String? = null
}
