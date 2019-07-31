package imdb.datatypes;

public class Revenue {
	public Long revenue;

	public Revenue() {}

	public Revenue(Long revenue) {
		super();
		this.revenue = revenue;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}

	//@Override
	public String toString() {
		if(revenue == null)
			return "NULL";
		return revenue.toString();
	}
}
