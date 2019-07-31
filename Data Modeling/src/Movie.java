import java.util.ArrayList;
import java.util.List;

import imdb.datatypes.*;
import imdb.datatypes.Runtime;

/*
 * The Movie class is for simple storage of IMDB Data 
 * extracted from IMDB csv file.  For each column
 * header from the csv, the movie class contains an
 * attribute that corresponds to the column datatype.
 * In addition, although it may be overkill, each
 * data field is itself an object of type 
 * "whatever the name of the column header is"
 * It will probably serve me later for keeping all this
 * data organized.
 */
public class Movie {
	final static int COLUMNS = 22;
	//Theres 7 lists to keep track of
	//movieRecord filled in by corresponding indexes
	private Object[] movieRecord;
	private Budget budget;	//0
	private List<Genre> genres;	//1
	private Homepage homepage;	//2
	private Id id;	//3
	private List<Keyword> keywords;	//4
	private Original_Language original_language; //5
	private Original_Title original_title;	//6
	private Overview overview;	//7
	private Popularity popularity;	//8
	private List<Production_Company> production_companies;	//9
	private List<Production_Country> production_countries;	//10
	private Release_Date release_date;	//11
	private Revenue revenue;	//12
	private Runtime runtime;	//13	
	private List<Spoken_Language> spoken_languages;	//14
	private Status status;	//15
	private Tagline tagline;	//16
	private Title title;	//17
	private Vote_Average vote_average;	//18
	private Vote_Count vote_count;	//19
	private List<Actor> cast; //20
	private Director director; //21
	/*
	 * Constructor
	 * instantiates the following fields.
	 * The rest will be initialized by the
	 * organizeMovieData() method.
	 */
	public Movie() {
		super();
		movieRecord = new Object[COLUMNS];
		genres = new ArrayList<Genre>();
		keywords = new ArrayList<Keyword>();
		production_companies = new ArrayList<Production_Company>();
		production_countries = new ArrayList<Production_Country>();
		spoken_languages = new ArrayList<Spoken_Language>();
	}

	/**
	 * Method fills in all the fields contained
	 * in a movie.  The end goal of this method
	 * is to properly organize all Movie data
	 * and ultimately, store each field into 
	 * an array that acts as a record of 
	 * IMDB movie data.
	 * The numbers in the comments correspond
	 * to the 20 attributes from the column headers
	 * and their instantiation
	 * IN ADDITION 2 MORE FROM CREDITS.CSV (cast, crew)
	 * 
	 * Params: raw string data from the IMDB csv,
	 * 22 columns
	 */
	public void organizeMovieData(String[] rawData) {
		int i;
		double d;
		//0  BUDGET
		if(rawData[0].isEmpty()) {
			budget =  new Budget(null);					//0 if empty
		}
		else {
			i = Integer.parseInt(rawData[0]); 
			budget =  new Budget(new Integer(i)); 		//0	
		}
		//1 GENRE
		if(rawData[1].equals("[]")) {
		//again not sure if I should put null or empty string
		//will use NULL FOR NOW AND TO STRINGS WILL PRINT "NULL"
			genres.add(new Genre(null,null));		  //1 if empty
		}
		else {
			generateList(rawData[1], new Genre()); 	//1 
		}
		//2 HOMEPAGE
		//again not sure if I should put null or empty string
		//will use NULL FOR NOW AND toString() WILL PRINT "NULL"
		if(rawData[2].isEmpty()) {
			homepage = new Homepage(null);  		//2 if empty
		}
		else {
			homepage = new Homepage(rawData[2]);  	//2
		}
		//3 ID
		if(rawData[3].isEmpty()) {
			id =  new Id(null);						//3 if empty
		}
		else {
			i = Integer.parseInt(rawData[3]); 
			id =  new Id(new Integer(i));  			//3	
		}
		//4 KEYWORD
		//again not sure if I should put null or empty string
		//will use NULL 
		if(rawData[4].equals("[]")) {
			keywords.add(new Keyword(null,null));	 //4 if empty
		}
		else {
			generateList(rawData[4], new Keyword()); //4
		}
		//5 ORIGINAL LANGUAGE
		if(rawData[5].equals("[]")) {
			original_language = new Original_Language(null);//5
		}
		else {
			original_language = new Original_Language(rawData[5]);	//5
		}
		//6 ORIGINAL TITLE
		if(rawData[6].isEmpty()) {
			original_title = new Original_Title(null);		//6 if empty
		}
		else {
			original_title = new Original_Title(rawData[6]);//6
		}
		//7 OVERVIEW
		if(rawData[7].isEmpty()) {
			overview = new Overview(null);  				//7 if empty
		}
		else {
			overview = new Overview(rawData[7]);    		//7
		}
		//8 POPULARITY
		if(rawData[8].isEmpty()) {
			popularity = new Popularity(null);			//8 in case runtime is empty
		}
		else {
			d = Double.parseDouble(rawData[8]);
			popularity = new Popularity(new Double(d)); //8
		}
		//9 PRODUCTION COMPANY		
		if(rawData[9].equals("[]")) {
			production_companies.add(new Production_Company(null, null));	//9 if empty
		}
		else {
			generateList(rawData[9], new Production_Company());				//9
		}
		//10 PRODUCTION COUNTRY
		if(rawData[10].equals("[]")) {
			production_countries.add(new Production_Country(null, null));	//10 if empty
		}
		else {
			generateList(rawData[10], new Production_Country());			//10
		}
		//11 RELEASE DATE
		if(rawData[11].isEmpty()) {
			release_date  = new Release_Date(null);							//11 if empty
		}
		else {
			release_date  = new Release_Date(rawData[11]);					//11
		}
		//12 REVENUE (probably do not need a long could use something else)
		if(rawData[12].isEmpty()) {
			revenue = new Revenue(null); 									//12 if empty
		}
		else {
			long long_num_for_revenue = Long.parseLong(rawData[12]);
			revenue = new Revenue(new Long(long_num_for_revenue)); 			//12
		}
		//13 RUNTIME
		//so there are several runtime fields that are empty
		//if its empty then insert null (again not sure if i should make "" or null
		//in general.  BUT FOR NOW ITS NULL!! for RUNTIME
		//WHICH MEANS I NEED TO MAKE ALL FIELDS THAT ARE EMPTY = NULL
		//FOR OTHER OBJECT TYPES
		if(rawData[13].isEmpty()) {
			runtime = new Runtime(null);							//13 in case runtime is empty
		}
		else {
			d = Double.parseDouble(rawData[13]);
			Double temp = d;
			runtime = new Runtime(new Integer(temp.intValue())); 	//13 if runtime has a value
		}
		//14 SPOKEN LANGUAGE
		if(rawData[14].equals("[]")) {
			spoken_languages.add(new Spoken_Language(null, null));	//14 if empty
		}
		else {
			generateList(rawData[14], new Spoken_Language());	   //14
		}
		//15 STATUS
		if(rawData[15].isEmpty()) {
			status = new Status(null);				//15 if empty
		}
		else {
			status = new Status(rawData[15]);		//15
		}
		//16 TAGLINE
		if(rawData[16].isEmpty()) {
			tagline = new Tagline(null); 			//16 if empty
		}
		else {
			tagline = new Tagline(rawData[16]); 	//16
		}
		//17 TITLE
		if(rawData[17].isEmpty()) {
			title = new Title(null);				//17 if empty
		}
		else {
			title = new Title(rawData[17]);			//17
		}
		//18 VOTE_AVERAGE
		if(rawData[18].isEmpty()) {
			vote_average = new Vote_Average(null);		    //18 if empty
		}
		else {
			d = Double.parseDouble(rawData[18]);
			vote_average = new Vote_Average(new Double(d));//18
		}
		//19 VOTE_COUNT
		if(rawData[19].isEmpty()) {
			vote_count = new Vote_Count(null);
		}
		else {
			i = Integer.parseInt(rawData[19]);
			vote_count = new Vote_Count(new Integer(i));//19
		}
		//FINALLY, INSERT ALL FIELDS (except actors and directors) INTO MOVIE RECORD!
		makeMovieRecord();
	}

	/*
	 * Essentially this is to get the actors.  In order to do this, I will split the string by commas.
	 * After splitting, I'll have a for loop and do the same within the brackets.
	 * I only need to get the id and name of the actor (-3 and -2 from end of index (like python end of list))
	 */
	public void addActorsAndDirector(String raw_cast, String raw_crew) {
		cast = new ArrayList<Actor>(); //20
		director = null; //21
		ArrayList<String> rawCastList = parseColumn(raw_cast);
		for(String ad: rawCastList) {
			if(ad == null) {
				Actor actor = new Actor(null, null);
				cast.add(actor);
				break;
			}
			String actor_data = ad.substring(ad.indexOf("\"gender\""), ad.length());
			String[] current = actor_data.split(",");
			String id = current[1];
			String name = current[2];
			String actor_id = id.substring(id.indexOf(":") + 1, id.length()).trim();
			String actor_name = name.substring(name.indexOf(":") + 1, name.length()).trim();
			Actor actor = new Actor(Integer.parseInt(actor_id), actor_name.replaceAll("\"", ""));
			cast.add(actor);
		}
		
		ArrayList<String> rawCrewList = parseColumn(raw_crew);
		for(String crew_data: rawCrewList) {
			if(crew_data == null) {
				director = new Director(null, null);
				break;
			}
			else if(crew_data.contains("\"job\": \"Director\", \"name\":")) {
				String[] current = crew_data.split(",");
				String id = current[3];
				String name = current[5];
				String dir_id = id.substring(id.indexOf(":") + 1, id.length()).trim();
				String dir_name = name.substring(name.indexOf(":") + 1, name.length()).trim();
				director = new Director(Integer.parseInt(dir_id), dir_name.replaceAll("\"", ""));
				break;
			}
		}
		
		movieRecord[20] = cast;	//20
		movieRecord[21] = director;	//21
	}

	/*
	 * This method takes all the movie fields and 
	 * places them nicely in an object array
	 * known as movieRecord!
	 * The movieRecord array will be used to
	 * display the movie data
	 */
	private void makeMovieRecord() {
		movieRecord[0]  = budget;	//0
		movieRecord[1]  = genres;	//1
		movieRecord[2]  = homepage;	//2
		movieRecord[3]  = id;		//3
		movieRecord[4]  = keywords;	//4
		movieRecord[5]  = original_language;//5
		movieRecord[6]  = title;//6
		movieRecord[7]  = overview;//7
		movieRecord[8]  = popularity;	//8
		movieRecord[9]  = production_companies;	//9
		movieRecord[10] = production_countries;	//10
		movieRecord[11] = release_date;	//11
		movieRecord[12] = revenue;	//12
		movieRecord[13] = runtime;	//13
		movieRecord[14] = spoken_languages;	//14
		movieRecord[15] = status;	//15
		movieRecord[16] = tagline;	//16
		movieRecord[17] = title;	//17
		movieRecord[18] = vote_average;	//18
		movieRecord[19] = vote_count;	//19
	}



/* SEEMS FINE BUT KEEP AN EYE ON IT
 * This method acts as the toString() method for the Movie class.
 * This may not work properly, so pay close attention and keep
 * an eye out on the printed results 
 */
	public void displayMovieListings() {
		if(movieRecord[20] == null || movieRecord == null) {
			System.out.println("We don't have the cast or director yet!");
			return;
		}
		System.out.println();
		int y = 1;
		for(Object obj : movieRecord) {
			if(obj instanceof List) {
				System.out.print(y + " [ ");
				String out = "";
				for (int i = 0; i < ((List<Object>) obj).size(); i ++) {
					out = ((List) obj).get(i).toString();
					if(i == ((List<Object>) obj).size() -1) {
						System.out.printf("%s ]", out);
					}
					else {
						System.out.printf("%s ", out);
					}
				}
				System.out.println();
			}
			else {
				System.out.printf("%d %s\t", y, obj.toString());
				System.out.println();
			}
			y++;
		}
	}

	/*
	 * This so far is the method to grab the id and name
	 * attributes for genres and keywords
	 * Looks awfully similar to prod companies, prod countries
	 * and spoken languages. may need to revisit
	 * params: raw string csv data and type of object passed
	 * to determine which to use (Genre or Keyword)
	 * for adding to their lists
	 */
	private void generateList(String rawData, Object obj) {
		ArrayList<String> rawList = parseColumn(rawData);
		putFieldsIntoList(rawList, obj);
	}

	/*
	 * PROBABLY MORE TO THIS TO REUSE FOR OTHER LISTS
	 * PROBABLY A BETTER WAY
	 * TOO MANY CHECKS TO SEE WHAT OBJECT IS USED
	 * This method properly parses the id and name fields from
	 * a genre or a keyword entry, depending on what type of
	 * obj is passed as a parameter
	 * params: list of raw data from the csv column
	 * param: obj - the type of object passed to determine which type of 
	 * list to add the id and name attributes
	 */
	private void putFieldsIntoList(ArrayList<String> rawList, Object obj) {
		//again not sure how to handle empty genres list 
		//either use null or "" but we will revisit this issue
		String field1 = "";
		String field2 = "";
		for (String s : rawList) {
			field1 = s.substring(s.indexOf(":") + 1, s.indexOf(","));
			field2 = s.substring(s.lastIndexOf(":") + 1, s.length());
			//System.out.print(id + " AND "  + name + "\n");
			field2 = field2.replace("\"", ""); //extract the comments from name

			//PROBABLY A BETTER WAY!!! SEEMS LIKE TOO MANY CHECKS HERE
			if(obj instanceof Keyword) {
				keywords.add(new Keyword(field1, field2));
			}
			else if(obj instanceof Production_Company) {
				production_companies.add(new Production_Company(field1, field2));
			}
			else if(obj instanceof Genre) {
				genres.add(new Genre(field1, field2));
			}
			else if(obj instanceof Production_Country) {
				production_countries.add(new Production_Country(field1, field2));
			}
			else if(obj instanceof Spoken_Language) {
				spoken_languages.add(new Spoken_Language(field1, field2));
			}
			
		}
	}

	/*
	 * Simple method to extract the string in between the open 
	 * and closing brackets for the keywords, genres
	 * extracted from the csv file.  Probably an 
	 * alternative way to do this for PRODUCTION_COMPANIES,
	 * PRODUCTION_COUNTRIES, and SPOKEN_LANGUAGES
	 * params: raw string data from the csv
	 */
	private static ArrayList<String> parseColumn(String rawData){
		ArrayList<String> rawGenresListPhase1 = new ArrayList<String>();
		int open = 0;	//for the open bracket
		int close = 0;	//for the close bracket
		String genreInformation = "";	//for getting the important parts

		//maybe we should add "" or add null but im not sure yet
		if(rawData.equals("[]")) {
			rawGenresListPhase1.add(null);	// for the empty genres lists
		}
		while(!rawData.isEmpty()) {
			open = rawData.indexOf("{");
			close = rawData.indexOf("}");
			if (open != -1 && close != -1) {
				genreInformation = rawData.substring(open + 1, close);
				rawGenresListPhase1.add(genreInformation);
				//System.out.println(message);
				rawData = rawData.substring(close+1, rawData.length());
				genreInformation = "";
			}
			else {
				break;
			}
		}
		return rawGenresListPhase1;
	}


	/*************************************************************************************************
	 * ALL CAPS BECAUSE I DON'T FEEL LIKE COMMENTING FOR GETS AND SETS!!!!!!
	 * GETTERS AND SETTERS FOR ALL FIELDS IN THE MOVIE CLASS
	 * PROBABLY NOT NECESSARY BUT WE WILL SEE
	 * 
	 */
	public Object[] getMovieRecord() {
		return movieRecord;
	}


	public void setMovieRecord(Object[] movieRecord) {
		this.movieRecord = movieRecord;
	}


	public Budget getBudget() {
		return budget;
	}


	public void setBudget(Budget budget) {
		this.budget = budget;
	}


	public ArrayList<Genre> getGenres() {
		return (ArrayList<Genre>) genres;
	}


	public void setGenre(ArrayList<Genre> genres) {
		this.genres = genres;
	}


	public Homepage getHomepage() {
		return homepage;
	}


	public void setHomepage(Homepage homepage) {
		this.homepage = homepage;
	}


	public Id getId() {
		return id;
	}


	public void setId(Id id) {
		this.id = id;
	}


	public ArrayList<Keyword> getKeywords() {
		return (ArrayList<Keyword>) keywords;
	}


	public void setKeywords(ArrayList<Keyword> keywords) {
		this.keywords = keywords;
	}


	public Original_Language getOriginal_language() {
		return original_language;
	}


	public void setOriginal_language(Original_Language original_language) {
		this.original_language = original_language;
	}


	public Original_Title getOriginal_title() {
		return original_title;
	}


	public void setOriginal_title(Original_Title original_title) {
		this.original_title = original_title;
	}


	public Overview getOverview() {
		return overview;
	}


	public void setOverview(Overview overview) {
		this.overview = overview;
	}


	public Popularity getPopularity() {
		return popularity;
	}


	public void setPopularity(Popularity popularity) {
		this.popularity = popularity;
	}


	public ArrayList<Production_Company> getProduction_companies() {
		return (ArrayList<Production_Company>) production_companies;
	}


	public void setProduction_companies(ArrayList<Production_Company> production_companies) {
		this.production_companies = production_companies;
	}


	public ArrayList<Production_Country> getProduction_countries() {
		return (ArrayList<Production_Country>) production_countries;
	}


	public void setProduction_countries(ArrayList<Production_Country> production_countries) {
		this.production_countries = production_countries;
	}


	public Release_Date getRelease_date() {
		return release_date;
	}


	public void setRelease_date(Release_Date release_date) {
		this.release_date = release_date;
	}


	public Revenue getRevenue() {
		return revenue;
	}


	public void setRevenue(Revenue revenue) {
		this.revenue = revenue;
	}


	public Runtime getRuntime() {
		return runtime;
	}


	public void setRuntime(Runtime runtime) {
		this.runtime = runtime;
	}


	public ArrayList<Spoken_Language> getSpoken_languages() {
		return (ArrayList<Spoken_Language>) spoken_languages;
	}


	public void setSpoken_languages(ArrayList<Spoken_Language> spoken_languages) {
		this.spoken_languages = (ArrayList<Spoken_Language>) spoken_languages;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public Tagline getTagline() {
		return tagline;
	}


	public void setTagline(Tagline tagline) {
		this.tagline = tagline;
	}


	public Title getTitle() {
		return title;
	}


	public void setTitle(Title title) {
		this.title = title;
	}


	public Vote_Average getVote_average() {
		return vote_average;
	}


	public void setVote_average(Vote_Average vote_average) {
		this.vote_average = vote_average;
	}


	public Vote_Count getVote_count() {
		return vote_count;
	}


	public void setVote_count(Vote_Count vote_count) {
		this.vote_count = vote_count;
	}


	public static int getColumns() {
		return COLUMNS;
	}

	public List<Actor> getCast() {
		return cast;
	}

	private void setCast(List<Actor> cast) {
		this.cast = cast;
	}

	public Director getDirector() {
		return director;
	}

	private void setDirector(Director director) {
		this.director = director;
	}
	
	
}
