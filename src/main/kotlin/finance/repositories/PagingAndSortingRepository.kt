package finance.repositories


import org.springframework.data.repository.CrudRepository
import java.io.Serializable


interface PagingAndSortingRepository<T, ID : Serializable> : CrudRepository<T, ID> {

    //https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
    //fun findOne(id: ID): T
    //internal override fun save(entity: T): T
    //fun findAll(sort: Sort): Iterable<T>

    //fun findAll(pageable: Pageable): Page<T>
 //PagingAndSortingRepository<User, Long> repository = // … get access to a bean
//Page<User> users = repository.findAll(new PageRequest(1, 20));
}