package imdb.datatypes;
/*
 * {"cast_id": 242, "character": "Jake Sully", "credit_id": "5602a8a7c3a3685532001c9a", "gender": 2, "id": 65731, "name": "Sam Worthington"}
 */
public class Actor {
	
	private Integer actor_id;
	private String actor_name;
	
	public Actor(Integer actor_id, String actor_name) {
		super();
		this.actor_id = actor_id;
		this.actor_name = actor_name;
	}
	public Integer getActor_id() {
		return actor_id;
	}
	public void setActor_id(Integer actor_id) {
		this.actor_id = actor_id;
	}
	public String getActor_name() {
		return actor_name;
	}
	public void setActor_name(String actor_name) {
		this.actor_name = actor_name;
	}
	
	@Override
	public String toString() {
		return "Actor [actor_id=" + actor_id + ", actor_name=" + actor_name + "]";
	}
	
}
