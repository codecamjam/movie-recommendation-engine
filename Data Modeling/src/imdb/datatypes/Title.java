package imdb.datatypes;

public class Title {
	private String title;

	public Title() {}

	public Title(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	//@Override
	public String toString() {
		if(title == null)
			return "NULL";
		return title;
	}
}
