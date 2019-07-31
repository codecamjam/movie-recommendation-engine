package imdb.datatypes;

public class Budget {
	private Integer budget;

	public Budget() {}

	public Budget(Integer budget) {
		super();
		this.budget = budget;
	}

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

	//@Override
	public String toString() {
		if(budget == null)
			return "NULL";
		return budget.toString();
	}
}
