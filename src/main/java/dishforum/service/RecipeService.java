package dishforum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dishforum.model.*;
import dishforum.repository.RecipeRepository;

@Service
public class RecipeService {

	private RecipeRepository recipeRepository;
	private DishService dishService;
	private CommentService commentService;
	
	@Autowired
	public RecipeService(RecipeRepository recipeRepository)
	{
		this.recipeRepository = recipeRepository;
	}
	
	@Autowired
	public void setDishService(DishService dishService)
	{
		this.dishService = dishService;
	}
	
	@Autowired
	public void setCommentService(CommentService commentService)
	{
		this.commentService = commentService;
	}
	
	public void addRecipeToDish(Recipe recipe, Long dish_id)
	{
		Dish dish = dishService.getDishById(dish_id);
		recipe.setDish(dish);
		recipeRepository.save(recipe);

	}
	
	public void updateRecipe(Recipe recipe)
	{
		recipeRepository.save(recipe);
	}
	
	public Recipe getRecipeById(Long id)
	{
		return recipeRepository.getRecipeById(id);
	}
	
	public void deleteRecipe(Recipe recipe, Long dish_id)
	{
		recipeRepository.delete(recipe);
	}
	
	public List<Recipe> getAllDishRecipes(Dish dish)
	{
		return recipeRepository.getAllByDish(dish);
	}
	
	public double countAvgRating(Recipe recipe)
	{
		double sum=0;
		for(Comment c : commentService.getAllRecipeComments(recipe))
		{
			sum = sum + c.getRating();
		}
		
		return sum/commentService.getAllRecipeComments(recipe).size();
	}
}
