package dishforum.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String user;
	private String content;
	@ManyToOne 
	@JoinColumn(name = "recipe_id")
	@JsonBackReference
	private Recipe recipe;
	private double rating;
	
	public Comment(String user, String content, Recipe recipe)
	{
		this.user = user;
		this.content = content;
		this.recipe = recipe;
	}
	
	public Comment() {};
}
