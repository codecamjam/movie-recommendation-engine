package imdb.datatypes;

//import java.net.URL;

public class Homepage {
	private String homepage = "";
	//private URL homepage;
	//URL url = null;

	public Homepage() {}

	public Homepage(String homepage) {
		super();
		this.homepage = homepage;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	//@Override
	public String toString() {
		if(homepage == null)
			return "NULL";
		return homepage;
	}
}
