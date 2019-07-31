package imdb.datatypes;

public class Overview {
	private String overview;

	public Overview() {}

	public Overview(String overview) {
		super();
		this.overview = overview;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	//@Override
	public String toString() {
		if(overview == null)
			return "NULL";
		return overview;
	}
}
