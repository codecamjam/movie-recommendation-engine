package imdb.datatypes;

public class Tagline {
	private String tag;

	public Tagline() {}

	public Tagline(String tag) {
		super();
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	//@Override
	public String toString() {
		if(tag == null)
			return "NULL";
		return tag;
	}
}
