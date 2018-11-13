package dishforum.controllertest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import dishforum.model.*;
import dishforum.service.RecipeService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private final static long id = 2;
    private final static Dish dish = new Dish();
    private final Recipe recipe = new Recipe("John", "RecipeExample", dish);
    private final Recipe updatedRecipe = new Recipe("Mark", "UpdatedRecipe", dish);

    @BeforeClass
    public static void initBeforeClass()
    {
    	dish.setId(id);
    }
    
    @Test
    public void getRecipeIfExist()
    {
    	given(recipeService.getRecipeById(id)).
    		willReturn(recipe);
    	
    	ResponseEntity<Recipe> recipeResponse = restTemplate.getForEntity("/dishes/2/recipes/2", Recipe.class);
    	assertThat(recipeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recipeResponse.getBody().equals(recipe));
    }
    
    @Test
    public void getRecipeIfNotExist()
    {
    	given(recipeService.getRecipeById(id)).willReturn(null);
    	
    	ResponseEntity<Recipe> recipeResponse = restTemplate.getForEntity("/dishes/2/recipes/2", Recipe.class);
    	
    	assertThat(recipeResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(recipeResponse.getBody()).isNull();
    }
    
    @Test
    public void addRecipe()
    {
        ResponseEntity<Recipe> recipeResponse = restTemplate.postForEntity("/dishes/2/recipes",
                recipe, Recipe.class);

        assertThat(recipeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    
    @Test
    public void updateRecipe()
    {	
    	given(recipeService.getRecipeById(id)).
    		willReturn(recipe);
    	HttpEntity<Recipe> requestEntity = new HttpEntity<Recipe>(updatedRecipe);

    	ResponseEntity<Recipe> recipeResponse = restTemplate.exchange("/dishes/2/recipes/2", HttpMethod.PUT, requestEntity, Recipe.class);

    	assertThat(recipeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	assertThat(recipeResponse.getBody().equals(updatedRecipe));
    }
    
    @Test
    public void deleteRecipe()
    {
    	given(recipeService.getRecipeById(id)).
			willReturn(recipe);
    	HttpEntity<Recipe> requestEntity = new HttpEntity<Recipe>(recipe);
    	
    	ResponseEntity<Recipe> recipeResponse = restTemplate.exchange("/dishes/2/recipes/2", HttpMethod.DELETE, requestEntity, Recipe.class);

    	assertThat(recipeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	assertThat(recipeResponse.getBody()).isNull();
    }
    
    @Test
    public void getAllRecipes()
    {
    	List<Recipe> recipeList = new ArrayList<>(); 
    	recipeList.add(recipe);
    	recipeList.add(updatedRecipe);
    	
    	given(recipeService.getAllDishRecipes(dish)).
			willReturn(recipeList);
    	
    	ResponseEntity<List<Recipe>> recipeResponse = restTemplate.exchange("/dishes/2/recipes", HttpMethod.GET, null, new ParameterizedTypeReference<List<Recipe>>(){});
    	assertThat(recipeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}