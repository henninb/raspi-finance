package finance.models

import javax.persistence.*
import javax.validation.constraints.Size

@Entity(name = "CategoryEntity")
@Table(name = "t_category")
class Category {
    constructor()

    constructor(category: String) {
        this.category = category
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var categoryId: Long = 0L
    @Size(max = 50)
    @Column(unique=true)
    var category: String? = null
}
