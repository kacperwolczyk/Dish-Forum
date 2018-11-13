package dishforum.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import dishforum.model.Dish;

@Repository
public interface DishRepository extends PagingAndSortingRepository<Dish, Long>{

	Dish getDishById(Long id);
	Dish getDishByName(String name);
}
