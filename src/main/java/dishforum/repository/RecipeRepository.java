package dishforum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dishforum.model.*;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>{

	Recipe getRecipeById(Long id);
	List<Recipe> getAllByDish(Dish dish);
	
}
