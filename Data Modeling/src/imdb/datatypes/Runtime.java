package imdb.datatypes;

public class Runtime {
	private Integer runtime;
	public Runtime() {}

	public Runtime(Integer runtime) {
		super();
		this.runtime = runtime;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	//@Override
	public String toString() {
		if (runtime == null) 
			return "NULL";
		return runtime.toString();
	}
}
