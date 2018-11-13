package dishforum.model;

import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String user;
	private String content;
	@OneToMany(mappedBy="recipe")
	@JsonManagedReference
	private List<Comment> comments;
	private double avgRating;
	@ManyToOne 
	@JoinColumn(name = "dish_id")
	@JsonBackReference
	private Dish dish;
	
	public Recipe(String user, String content, Dish dish)
	{
		this.user = user;
		this.content = content;
		this.dish = dish;
	}
	
	public Recipe() {}
}
