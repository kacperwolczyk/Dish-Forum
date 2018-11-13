package dishforum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dishforum.model.*;
import dishforum.repository.CommentRepository;

@Service
public class CommentService {

	private CommentRepository commentRepository;
	private RecipeService recipeService;
	
	@Autowired
	public CommentService(CommentRepository commentRepository)
	{
		this.commentRepository = commentRepository;
	}
	
	@Autowired
	public void setRecipeService(RecipeService recipeService)
	{
		this.recipeService = recipeService;
	}
	
	public void addCommentToRecipe(Long id, Comment comment)
	{
		Recipe recipe = recipeService.getRecipeById(id);
		comment.setRecipe(recipe);
		commentRepository.save(comment);
		recipe.setAvgRating(recipeService.countAvgRating(recipe));
		recipeService.updateRecipe(recipe);
	}
	
	public void updateComment(Comment comment)
	{
		commentRepository.save(comment);
		Recipe recipe = comment.getRecipe();
		recipe.setAvgRating(recipeService.countAvgRating(recipe));
		recipeService.updateRecipe(recipe);
	}
	
	public Comment getCommentById(Long id)
	{
		return commentRepository.getCommentById(id);
	}
	
	public void deleteComment(Comment comment, Long id)
	{
		Recipe recipe = recipeService.getRecipeById(id);
		commentRepository.delete(comment);
		recipe.setAvgRating(recipeService.countAvgRating(recipe));
		recipeService.updateRecipe(recipe);
	}
	
	public List<Comment> getAllRecipeComments(Recipe recipe)
	{
		return commentRepository.getAllByRecipe(recipe);
	}
	
}
