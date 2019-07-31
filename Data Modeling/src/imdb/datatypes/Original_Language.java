package imdb.datatypes;

public class Original_Language {
	private String language;

	public Original_Language() {}

	public Original_Language(String language) {
		super();
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	//@Override
	public String toString() {
		if(language == null)
			return "NULL";
		return language;
	}
}
