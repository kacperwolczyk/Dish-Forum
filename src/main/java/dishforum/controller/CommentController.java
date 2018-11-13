package dishforum.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import dishforum.model.*;
import dishforum.service.*;


@RestController
@RequestMapping("/dishes/{dish_id}/recipes/{recipe_id}/comments")
public class CommentController {

	private CommentService commentService;
	private RecipeService recipeService;
	
	@Autowired
	public CommentController(CommentService commentService)
	{
		this.commentService = commentService;
	}
	
	@Autowired
	public void setRecipeService(RecipeService recipeService)
	{
		this.recipeService = recipeService;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long recipe_id)
	{
		Recipe recipe = recipeService.getRecipeById(recipe_id);
		List<Comment> comments = commentService.getAllRecipeComments(recipe);

		if(comments.isEmpty())
			return new ResponseEntity<List<Comment>>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
	}
	
	@GetMapping(path="/{com_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> getComment(@PathVariable Long com_id)
	{
		Comment comment = commentService.getCommentById(com_id);
		if(comment==null)
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Comment>(comment, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> saveComment(@PathVariable Long recipe_id, @RequestBody Comment comment, UriComponentsBuilder ucBuilder) 
	{
		Recipe recipe = recipeService.getRecipeById(recipe_id);
		if(commentService.getAllRecipeComments(recipe).contains(comment))
			return new ResponseEntity<Comment>(HttpStatus.CONFLICT);
		
		commentService.addCommentToRecipe(recipe_id, comment);
		
	    HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/{comment_id}").buildAndExpand(comment.getId()).toUri());
        return new ResponseEntity<Comment>(headers, HttpStatus.CREATED);
	}
	
	@DeleteMapping(path="/{com_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> deleteComment(@PathVariable Long com_id, @PathVariable Long recipe_id)
	{
		Comment comment = commentService.getCommentById(com_id);
		if(comment==null)
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		
		commentService.deleteComment(comment, recipe_id);
		return new ResponseEntity<Comment>(HttpStatus.OK);
	}
	
	@PutMapping(path="/{com_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> updateComment(@PathVariable Long com_id, @PathVariable Long recipe_id, @RequestBody Comment comment)
	{
		Comment updatedComment = commentService.getCommentById(com_id);
		if(updatedComment==null)
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		
		updatedComment.setContent(comment.getContent());
		
		commentService.updateComment(updatedComment);
		return new ResponseEntity<Comment>(updatedComment, HttpStatus.OK);
	}
}
