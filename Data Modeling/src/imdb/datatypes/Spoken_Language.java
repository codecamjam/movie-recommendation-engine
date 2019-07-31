package imdb.datatypes;

public class Spoken_Language {
	private String iso;
	private String name;

	public Spoken_Language() {}

	public Spoken_Language(String iso, String name) {
		super();
		
		if(iso != null && name != null) {
			this.iso = iso.trim();
			this.name = name.trim();
		}
		else {
			this.iso = iso;
			this.name = name;
		}
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//@Override
	public String toString() {
		if(iso == null) 
			return "NULL";
		return "{\"iso_3166_1:\""+iso+",\"name\":\""+name+"\"}";
	}


}
