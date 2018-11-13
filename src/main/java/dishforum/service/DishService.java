package dishforum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import dishforum.model.Dish;
import dishforum.repository.DishRepository;

@Service
public class DishService {

	private DishRepository dishRepository;
	
	@Autowired
	public DishService(DishRepository dishRepository)
	{
		this.dishRepository = dishRepository;
	}
	
	
	public void addDish(Dish dish)
	{
		dishRepository.save(dish);
	}
	
	public void updateDish(Dish dish)
	{
		dishRepository.save(dish);
	}
	
	public Dish getDishById(Long id)
	{
		return dishRepository.getDishById(id);
	}
	
	public Dish getDishByName(String name)
	{
		return dishRepository.getDishByName(name);
	}
	
	public void deleteDish(Dish dish)
	{
		dishRepository.delete(dish);
	}
	
	public List<Dish> getAllDishes(int page, int limit, String orderBy)
	{
		Sort sort = new Sort(Direction.ASC, orderBy);
		Pageable pageableRequest = PageRequest.of(page, limit, sort);
		Page<Dish> dishes = dishRepository.findAll(pageableRequest);
		List<Dish> dishList = dishes.getContent();
		
		return dishList;
	}
}
