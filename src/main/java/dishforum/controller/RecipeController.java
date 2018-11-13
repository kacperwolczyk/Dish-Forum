package dishforum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import dishforum.model.*;
import dishforum.service.*;


@RestController
@RequestMapping("/dishes/{dish_id}/recipes")
public class RecipeController {

	private RecipeService recipeService;
	private DishService dishService;
	
	@Autowired
	public RecipeController(RecipeService recipeService)
	{
		this.recipeService = recipeService;
	}
	
	@Autowired
	public void setDishService(DishService dishService)
	{
		this.dishService = dishService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Recipe>> getAllRecipes(@PathVariable Long dish_id)
	{
		Dish dish = dishService.getDishById(dish_id);
		List<Recipe> recipes = recipeService.getAllDishRecipes(dish);
		if(recipes.isEmpty())
			return new ResponseEntity<List<Recipe>>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<Recipe>>(recipes, HttpStatus.OK);
	}
	
	@GetMapping(path="/{recipe_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> getRecipe(@PathVariable Long recipe_id)
	{
		Recipe recipe = recipeService.getRecipeById(recipe_id);
		if(recipe==null)
			return new ResponseEntity<Recipe>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> saveRecipe(@PathVariable Long dish_id, @RequestBody Recipe recipe, UriComponentsBuilder ucBuilder) 
	{
		Dish dish = dishService.getDishById(dish_id);
		if(recipeService.getAllDishRecipes(dish).contains(recipe))
			return new ResponseEntity<Recipe>(HttpStatus.CONFLICT);
		
		recipeService.addRecipeToDish(recipe, dish_id);
		
	    HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/{recipe_id}").buildAndExpand(recipe.getId()).toUri());
        return new ResponseEntity<Recipe>(headers, HttpStatus.CREATED);
	}
	
	@DeleteMapping(path="/{recipe_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> deleteRecipe(@PathVariable Long recipe_id, @PathVariable Long dish_id)
	{
		Recipe recipe = recipeService.getRecipeById(recipe_id);
		if(recipe==null)
			return new ResponseEntity<Recipe>(HttpStatus.NOT_FOUND);
		
		recipeService.deleteRecipe(recipe, dish_id);
		return new ResponseEntity<Recipe>(HttpStatus.OK);
	}
	
	@PutMapping(path="/{recipe_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> updateRecipe(@PathVariable Long dish_id, @PathVariable Long recipe_id, @RequestBody Recipe recipe)
	{
		Recipe updatedRecipe = recipeService.getRecipeById(recipe_id);
		if(updatedRecipe==null)
			return new ResponseEntity<Recipe>(HttpStatus.NOT_FOUND);
		
		updatedRecipe.setContent(recipe.getContent());
		
		recipeService.updateRecipe(updatedRecipe);
		return new ResponseEntity<Recipe>(updatedRecipe, HttpStatus.OK);
	}
	
}
