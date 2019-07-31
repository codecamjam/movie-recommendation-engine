package imdb.datatypes;

public class Id {
	public Integer id;

	public Id() {}

	public Id(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	//@Override
	public String toString() {
		if(id == null)
			return "NULL";
		return id.toString();
	}
}
