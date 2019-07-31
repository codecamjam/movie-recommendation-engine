package imdb.datatypes;

public class Vote_Count {

	private Integer count;

	public Vote_Count() {}

	public Vote_Count(Integer count) {
		super();
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	//@Override
	public String toString() {
		if(count == null)
			return "NULL";
		return count.toString();
	}
}
