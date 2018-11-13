package dishforum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dishforum.model.*;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	Comment getCommentById(Long id);
	List<Comment> getAllByRecipe(Recipe recipe);
}
