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
import dishforum.service.CommentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private final static long id = 2;
    private final static Dish dish = new Dish();
    private final static Recipe recipe = new Recipe();
    private final Comment comment = new Comment("Mike", "CommentExample", recipe);
    private final Comment updatedComment = new Comment("Josh", "UpdatedExample", recipe);

    @BeforeClass
    public static void initBeforeClass()
    {
    	dish.setId(id);
    	recipe.setId(id);
    	recipe.setDish(dish);
    }
    
    @Test
    public void getCommentIfExist()
    {
    	given(commentService.getCommentById(id)).
    		willReturn(comment);
    	
    	ResponseEntity<Comment> commentResponse = restTemplate.getForEntity("/dishes/2/recipes/2/comments/2", Comment.class);

    	assertThat(commentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(commentResponse.getBody().equals(comment));
    }
    
    @Test
    public void getCommentIfNotExist()
    {
    	given(commentService.getCommentById(id)).willReturn(null);
    	
    	ResponseEntity<Comment> commentResponse = restTemplate.getForEntity("/dishes/2/recipes/2/comments/2", Comment.class);
    	
    	assertThat(commentResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(commentResponse.getBody()).isNull();
    }
    
    @Test
    public void addComment()
    {
        ResponseEntity<Comment> commentResponse = restTemplate.postForEntity("/dishes/2/recipes/2/comments",
                recipe, Comment.class);

        assertThat(commentResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    
    @Test
    public void updateComment()
    {	
    	given(commentService.getCommentById(id)).
    		willReturn(comment);
    	HttpEntity<Comment> requestEntity = new HttpEntity<Comment>(updatedComment);

    	ResponseEntity<Comment> commentResponse = restTemplate.exchange("/dishes/2/recipes/2/comments/2", HttpMethod.PUT, requestEntity, Comment.class);

    	assertThat(commentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	assertThat(commentResponse.getBody().equals(updatedComment));
    }
    
    @Test
    public void deleteComment()
    {
    	given(commentService.getCommentById(id)).
			willReturn(comment);
    	HttpEntity<Comment> requestEntity = new HttpEntity<Comment>(comment);
    	
    	ResponseEntity<Comment> commentResponse = restTemplate.exchange("/dishes/2/recipes/2/comments/2", HttpMethod.DELETE, requestEntity, Comment.class);

    	assertThat(commentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	assertThat(commentResponse.getBody()).isNull();
    }
    
    @Test
    public void getAllComments()
    {
    	List<Comment> commentList = new ArrayList<>(); 
    	commentList.add(comment);
    	commentList.add(updatedComment);

    	given(commentService.getAllRecipeComments(recipe)).
			willReturn(commentList);
    	
    	ResponseEntity<List<Comment>> commentResponse = restTemplate.exchange("/dishes/2/recipes/2/comments", HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>(){});
    	assertThat(commentResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
