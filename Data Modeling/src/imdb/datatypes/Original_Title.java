package imdb.datatypes;

public class Original_Title {

	private String title;

	public Original_Title() {}

	public Original_Title(String title) {
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
