package dishforum.controllertest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import dishforum.model.Dish;
import dishforum.service.DishService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DishControllerTest {

    @MockBean
    private DishService dishService;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private final long id = 2;
    private final Dish dish = new Dish("Pizza", "dinner");
    private final Dish updatedDish = new Dish("Pizza", "breakfast");
    
    
    @Test
    public void getDishIfExist()
    {
    	given(dishService.getDishById(id)).
    		willReturn(dish);
    	
    	ResponseEntity<Dish> dishResponse = restTemplate.getForEntity("/dishes/2", Dish.class);
    	assertThat(dishResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(dishResponse.getBody().equals(dish));
    }
    
    @Test
    public void getDishIfNotExist()
    {
    	given(dishService.getDishById(id)).willReturn(null);
    	
    	ResponseEntity<Dish> dishResponse = restTemplate.getForEntity("/dishes/2", Dish.class);
    	
    	assertThat(dishResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(dishResponse.getBody()).isNull();
    }
    
    @Test
    public void addDish()
    {
        ResponseEntity<Dish> dishResponse = restTemplate.postForEntity("/dishes",
                dish, Dish.class);

        assertThat(dishResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    
    @Test
    public void updateDish()
    {	
    	given(dishService.getDishById(id)).
    		willReturn(dish);
    	HttpEntity<Dish> requestEntity = new HttpEntity<Dish>(updatedDish);
    	
    	ResponseEntity<Dish> dishResponse = restTemplate.exchange("/dishes/2", HttpMethod.PUT, requestEntity, Dish.class);

    	assertThat(dishResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	assertThat(dishResponse.getBody().equals(updatedDish));
    }
    
    @Test
    public void deleteDish()
    {
    	given(dishService.getDishById(id)).
			willReturn(dish);
    	HttpEntity<Dish> requestEntity = new HttpEntity<Dish>(dish);
    	
    	ResponseEntity<Dish> dishResponse = restTemplate.exchange("/dishes/2", HttpMethod.DELETE, requestEntity, Dish.class);

    	assertThat(dishResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	assertThat(dishResponse.getBody()).isNull();
    }
    
    @Test
    public void getAllDishes()
    {
    	
    	ResponseEntity<List<Dish>> dishResponse = restTemplate.exchange("/dishes", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Dish>>(){});
    	assertThat(dishResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
}
