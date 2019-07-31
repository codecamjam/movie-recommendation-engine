package imdb.datatypes;

public class Vote_Average {

	private Double average;

	public Vote_Average() {}

	public Vote_Average(Double average) {
		super();
		this.average = average;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	//@Override
	public String toString() {
		if(average == null)
			return "NULL";
		return average.toString();
	}

}
