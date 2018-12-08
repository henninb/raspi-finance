package finance.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.Size
import java.util.HashSet



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

    //@JsonIgnore
    //@ManyToMany(mappedBy = "categories")
    //var transactions = mutableListOf<Transaction>()
}
