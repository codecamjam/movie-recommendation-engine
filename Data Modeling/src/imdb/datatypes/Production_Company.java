package imdb.datatypes;

public class Production_Company {
	private String name;
	private String id;

	public Production_Company() {}

	public Production_Company(String name, String id) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	//@Override
	public String toString() {
		
		if(name == null)
			return "NULL";
		return "{\"name:\""+name+",\"id\":\""+id+"\"}";

	}


}
