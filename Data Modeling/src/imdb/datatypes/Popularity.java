package imdb.datatypes;

public class Popularity {
	private Double popularity;

	public Popularity() {}

	public Popularity(Double popularity) {
		super();
		this.popularity = popularity;
	}

	public Double getPopularity() {
		return popularity;
	}

	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}

	//@Override
	public String toString() {
		if(popularity == null)
			return "NULL";
		return popularity.toString();
	}
}
