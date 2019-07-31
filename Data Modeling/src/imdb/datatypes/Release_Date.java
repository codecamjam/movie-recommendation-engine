package imdb.datatypes;

public class Release_Date {
	private String date;

	public Release_Date() {}

	public Release_Date(String date) {
		super();
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	//@Override
	public String toString() {
		if(date == null)
			return "NULL";
		return date;
	}
}
