package dishforum.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Dish {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String category;
	@OneToMany(mappedBy="dish")
	@JsonManagedReference
	private List<Recipe> recipes;

	
	public Dish (String name, String category)
	{
		this.name = name;
		this.category = category;
	}
	
	public Dish() {}

}
