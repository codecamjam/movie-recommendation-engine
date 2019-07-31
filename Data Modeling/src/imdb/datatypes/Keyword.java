package imdb.datatypes;

public class Keyword {
	
	private String id;
	private String name;

	public Keyword() {}

	public Keyword(String id, String name) {
		super();
		if(id != null && name != null) {
			this.id = id.trim();
			this.name = name.trim();
		}
		else {
			this.id = id;
			this.name = name;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//@Override
	public String toString() {
		if(id == null)
			return "NULL";
		return "{id: "+id+", name: "+name+"}";

	}
}