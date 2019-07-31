package imdb.datatypes;

public class Status {

	private String status;

	public Status() {}

	public Status(String status) {
		super();
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	//@Override
	public String toString() {
		if(status == null)
			return "NULL";
		return status;
	}

}
