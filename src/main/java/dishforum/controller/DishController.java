package dishforum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import dishforum.model.*;
import dishforum.service.DishService;

@RestController
@RequestMapping("/dishes")
public class DishController {

	private DishService dishService;
	
	@Autowired
	public DishController (DishService dishService)
	{
		this.dishService = dishService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Dish>> getAllDishes(
            @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="5") int limit, @RequestParam(defaultValue="name") String sortBy)
	{
		List<Dish> dishes = dishService.getAllDishes(page-1, limit, sortBy);
		if(dishes.isEmpty())
			return new ResponseEntity<List<Dish>>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<List<Dish>>(dishes, HttpStatus.OK);

	}
	
	@GetMapping(path="/{dish_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Dish> getDish(@PathVariable Long dish_id)
	{
		Dish dish = dishService.getDishById(dish_id);
		if(dish==null)
			return new ResponseEntity<Dish>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Dish>(dish, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Dish> saveDish(@RequestBody Dish dish, UriComponentsBuilder ucBuilder) 
	{
	    
	    dishService.addDish(dish);
	    
	    HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/{dish_id}").buildAndExpand(dish.getId()).toUri());
        return new ResponseEntity<Dish>(headers, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping(path="/{dish_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Dish>deleteDish (@PathVariable Long dish_id)
	{
		Dish dish = dishService.getDishById(dish_id);
		if(dish==null)
			return new ResponseEntity<Dish>(HttpStatus.NOT_FOUND);
		
		dishService.deleteDish(dish);
		return new ResponseEntity<Dish>(HttpStatus.OK);
	}
	
	@PutMapping(path="/{dish_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Dish> updateDish (@PathVariable Long dish_id, @RequestBody Dish dish)
	{
		Dish updatedDish = dishService.getDishById(dish_id);
		if(updatedDish==null)
			return new ResponseEntity<Dish>(HttpStatus.NOT_FOUND);
		
		updatedDish.setCategory(dish.getCategory());
		updatedDish.setName(dish.getName());
		
		dishService.updateDish(updatedDish);
		return new ResponseEntity<Dish>(updatedDish, HttpStatus.OK);
	}
}
