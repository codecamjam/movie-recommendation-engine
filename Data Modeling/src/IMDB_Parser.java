
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import imdb.datatypes.Actor;
import imdb.datatypes.Genre;
import imdb.datatypes.Keyword;


/*
 * CAMERON TAGHLABI
 * IMDB_Parser serves as the Java file that
 * will perform the parsing of an IMDB movie data csv,
 * a messy collection of movie data consisting of 
 * almost 5000 rows of movie information.
 * IMDB_Parser will take this data and parse it
 * with the help of Apache Commons CSV library.
 */
public class IMDB_Parser {
	static final String DB_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone= " + TimeZone.getDefault().getID();
	//Use your own MySQL username and password!
	static final String USER = "root";
	static final String PASS = "root";
	static final int COLUMNS = 22;

	/*
	 * To make our lives easier, Apache Commons CSV library
	 * has been included in order to not make parsing an
	 * unorganized CSV file a nightmare.  CSVParser parser
	 * simply takes the csv file and formats the columns
	 * by the header fields at the top of the csv file.
	 * For each row in the csv, a movie object is instantiated.
	 * The movie and current csv record is passed to the
	 * getMovieInfo method, which takes the raw string text
	 * data from each column field and stores in a string array.
	 * That string array is then passed to the movie object
	 * for further parsing and organization.
	 */
	public static void main(String[] args) {
		String filename = "tmdb_5000_movies.csv";
		String cred = "tmdb_5000_credits.csv";
		List<Movie> moviesList = new ArrayList<Movie>();
		try {
			System.out.println("One moment while data is being prepared...");
			CSVParser movie_parser = new CSVParser(new FileReader(filename), 
					CSVFormat.DEFAULT.withHeader());
			for (CSVRecord m_record : movie_parser) {
				Movie movie = new Movie();
				getMovieInfo(movie, m_record);
				moviesList.add(movie);
			}
			movie_parser.close();
			//Initially, I did not realize there were 2 CSV files to parse. 
			//The 2nd CSVParser is used to get the cast and crew for each movie
			CSVParser credit_parser = new CSVParser(new FileReader(cred), 
					CSVFormat.DEFAULT.withHeader());
			int i = 0;
			for (CSVRecord c_record : credit_parser) {
				getCastAndCrew(moviesList.get(i++), c_record);
			}
			credit_parser.close();	
			//searchForMovie(moviesList);

			fillDatabase(moviesList);
		}
		catch (Exception e) {
			//System.out.println("LINE NO: " + lineno);
			e.printStackTrace();
		}
	}
	
	/*
	 * This method takes the raw text data from the csv, 
	 * and each record.get() corresponds to the data 
	 * contained in the column current column.
	 * The 20 column data entries are stored in a 
	 * string array.  The string array is passed to the 
	 * organizeMovieData() method, instantiated by
	 * the movie object passed.  The last line
	 * will essentially parse the data from the csv
	 * file.
	 * Param: Movie movie is the Movie instance variable
	 * Param: CSVRecord record is the CSVRecord instance variable
	 */
	public static void getMovieInfo(Movie movie, CSVRecord record) {
		String[] rawData = new String[COLUMNS];
		rawData[0]  = record.get("budget").toString();
		rawData[1]  = record.get("genres").toString();
		rawData[2]  = record.get("homepage").toString();
		rawData[3]  = record.get("id").toString();
		rawData[4]  = record.get("keywords").toString();
		rawData[5]  = record.get("original_language").toString();
		rawData[6]  = record.get("original_title").toString();
		rawData[7]  = record.get("overview").toString();
		rawData[8]  = record.get("popularity").toString();
		rawData[9]  = record.get("production_companies").toString();
		rawData[10] = record.get("production_countries").toString();
		rawData[11] = record.get("release_date").toString();
		rawData[12] = record.get("revenue").toString();
		rawData[13] = record.get("runtime").toString();
		rawData[14] = record.get("spoken_languages").toString();
		rawData[15] = record.get("status").toString();
		rawData[16] = record.get("tagline").toString();
		rawData[17] = record.get("title").toString();
		rawData[18] = record.get("vote_average").toString();
		rawData[19] = record.get("vote_count").toString();
		movie.organizeMovieData(rawData);
	}

	/*
	 * Parse the cast and crew data and add to each movie
	 */
	private static void getCastAndCrew(Movie movie, CSVRecord record) {
		String cast = record.get("cast").toString();
		String crew = record.get("crew").toString();
		movie.addActorsAndDirector(cast, crew);
	}


	/*
	 * Method fills all tables associated with IMDB movie database
	 */
	public static void fillDatabase(List<Movie> ml) {
		Connection conn = null; 
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			String sql = "CREATE DATABASE imdb";
			stmt.executeUpdate(sql);
			System.out.println("Database created.");
			sql = "USE imdb";
			stmt.executeUpdate(sql);

			createTables(conn, stmt);
			fillMovieTable(conn, ml, stmt); //1
			fillActorTable(conn, ml, stmt); //2
			fillDirectorTable(conn, ml, stmt); //3
			fillGenreTable(conn, ml, stmt); //4
			fillKeywordTable(conn, ml, stmt); //5
			fillmovieActorsTable(conn, ml, stmt); //6 
			fillMovieDirectorsTable(conn, ml, stmt); //7
			fillMovieGenresTable(conn, ml, stmt); //8
			fillMovieKeywordsTable(conn, ml, stmt);  //9
			getUserQueries(stmt, ml);

			stmt = conn.createStatement();
			sql = "DROP DATABASE imdb";
			stmt.executeUpdate(sql);
			System.out.println("Database dropped.");
			stmt.close();
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally { 
			try {
				if(conn!=null) { 
					conn.close();
				}
			}
			catch(SQLException se) {
				se.printStackTrace();
			} 
		}
		System.out.println("Run complete. Shutting down.");
	}
	
	/*
	 * This method creates all the tables for my imdb database.  I need to consider how to include
	 * the keyword count into my keywords table.  Also, I am not sure how important the constraints
	 * will be when dealing with this project.  For now, the constraints will be minimal.
	 */
	private static void createTables(Connection conn, Statement stmt) throws SQLException {
		String sql = "";
		//1
		sql = "CREATE TABLE movie(mid INT UNSIGNED, release_date DATE, "
				+ "title VARCHAR(90), popularity DOUBLE, vote_avg DOUBLE, vote_count INT UNSIGNED, PRIMARY KEY (mid))";
		stmt.executeUpdate(sql);
		System.out.println("Table movie created");

		//2
		sql = "CREATE TABLE director(dir_id INT UNSIGNED, dir_name VARCHAR(45), PRIMARY KEY (dir_id))";
		stmt.executeUpdate(sql);
		System.out.println("Table director created");

		//3
		sql = "CREATE TABLE movieDirectors(mid INT UNSIGNED, dir_id INT UNSIGNED, "
				+ "PRIMARY KEY (mid, dir_id))";
		stmt.executeUpdate(sql);
		System.out.println("Table movieDirectors created");

		//4
		sql = "CREATE TABLE actor(act_id INT UNSIGNED, actor_name VARCHAR(40), "
				+ "PRIMARY KEY (act_id))";
		stmt.executeUpdate(sql);
		System.out.println("Table actor created");

		//5
		sql = "CREATE TABLE movieActors(mid INT UNSIGNED, act_id INT UNSIGNED, "
				+ "PRIMARY KEY (mid, act_id))";
		stmt.executeUpdate(sql);
		System.out.println("Table movieActors created");

		//6
		sql = "CREATE TABLE genre(gid INT UNSIGNED, genre VARCHAR(100), "
				+ "PRIMARY KEY (gid))";
		stmt.executeUpdate(sql);
		System.out.println("Table genre created");

		//7
		sql = "CREATE TABLE movieGenres(mid INT UNSIGNED, gid INT UNSIGNED, "
				+ "PRIMARY KEY (mid, gid))";
		stmt.executeUpdate(sql);
		System.out.println("Table movieGenres created");

		//8
		sql = "CREATE TABLE keyword(kwid INT UNSIGNED, keyword VARCHAR(100), "
				+ "count INT UNSIGNED, PRIMARY KEY (kwid))";
		stmt.executeUpdate(sql);
		System.out.println("Table keyword created");

		//9
		sql = "CREATE TABLE movieKeywords(mid INT UNSIGNED, kwid INT UNSIGNED, "
				+ "PRIMARY KEY (mid, kwid))";
		stmt.executeUpdate(sql);
		System.out.println("Table movieKeywords created");

	}
	

	/*
	 * Essentially just method to take user input.
	 * User will search for a movie title and 
	 * the method will return all 20 fields
	 * of movie information. Program ends when
	 * user types 'quit'.  Input is case insensitive.
	 */
	private static void getUserQueries(Statement stmt, List<Movie> ml) throws SQLException { 
		System.out.println();
		String sql = "";
		ResultSet myRS = null;
		Map<Integer, Integer> top5 = new HashMap<>(); 
		boolean found = false;
		Scanner input = new Scanner(System.in);
		String user_movie = "";
		while(!user_movie.equalsIgnoreCase("quit")) {
			found = false;
			System.out.print("Enter a movie title"
					+ "(case sensitive)\nINPUT: ");
			user_movie = input.nextLine();
			System.out.println();
			if(user_movie.equalsIgnoreCase("quit")) {
				System.out.println("Bye");
				return;
			}
			else {
				sql = "SELECT mid FROM movie WHERE title='" + user_movie + "';";
				myRS = stmt.executeQuery(sql);
				if (myRS.next() == false) {
					//System.out.println("---------------------------------------------------------------------------------------------------------------");
					System.out.println(user_movie + " was not found. Try again");
					//System.out.println("---------------------------------------------------------------------------------------------------------------");
				} 
				else {
					ArrayList<Integer>[] movie_matrix = new ArrayList[4];
					int mid = myRS.getInt("mid");
					
					Movie get_user_movie_data = null;
					for(int b = 0; b < ml.size(); b++) {
						Movie m = ml.get(b);
						Integer num = m.getId().getId();
						if(num == mid) {
							get_user_movie_data = m;
							break;
						}
					}
					
					//GET GENRES:
					sql = "SELECT g.gid FROM genre g, movieGenres mg, movie m " +
							"WHERE m.mid=mg.mid AND g.gid=mg.gid AND m.title='" + user_movie + "';";
					myRS = stmt.executeQuery(sql);
					ArrayList<Integer> genreIDs = new ArrayList<>();
					if (myRS.next() == false) {	
						genreIDs.add(0);
					}
					else {
						do {
							int cur = myRS.getInt("g.gid");
							genreIDs.add(cur);
						} while (myRS.next());
					}
					movie_matrix[0] = genreIDs;
					
					//GET KEYWORDS:
					//IF KEYWORD COUNT > 4
//					sql = "SELECT k.kwid FROM keyword k, movieKeywords mk, movie m " +
//							"WHERE m.mid=mk.mid AND k.kwid=mk.kwid AND k.count > 4 AND m.title='" + user_movie + "';";
					sql = "SELECT k.kwid FROM keyword k, movieKeywords mk, movie m " +
							"WHERE m.mid=mk.mid AND k.kwid=mk.kwid AND m.title='" + user_movie + "';";
					myRS = stmt.executeQuery(sql);
					ArrayList<Integer> keywordIDs = new ArrayList<>();
					//IF KEYWORD COUNT> 4 is an empty set
					if (myRS.next() == false) {
						keywordIDs.add(0);
//						sql = "SELECT k.kwid FROM keyword k, movieKeywords mk, movie m " +
//								"WHERE m.mid=mk.mid AND k.kwid=mk.kwid AND m.title='" + user_movie + "';";
//						myRS = stmt.executeQuery(sql);
//						if (myRS.next() == false) {
//							keywordIDs.add(0);
//						}
//						else {
//							do {
//								int cur = myRS.getInt("k.kwid");
//								keywordIDs.add(cur);
//							} while (myRS.next());
//						}
					}
					else {
						do {
							int cur = myRS.getInt("k.kwid");
							keywordIDs.add(cur);
						} while (myRS.next());
					}
					movie_matrix[1] = keywordIDs;
					
					//GET DIRECTORS:
					sql = "SELECT d.dir_id FROM director d, movieDirectors md, movie m " +
							"WHERE m.mid=md.mid AND md.dir_id=d.dir_id AND m.title='" + user_movie + "';";
					myRS = stmt.executeQuery(sql);
					ArrayList<Integer> directorIDs = new ArrayList<>(); 
					if (myRS.next() == false) {
						directorIDs.add(0);
					}
					else {
						do {
							int cur = myRS.getInt("d.dir_id");
							directorIDs.add(cur);
						} while (myRS.next());
					}
					movie_matrix[2] = directorIDs;
					
					//GET ACTORS:
					sql = "SELECT a.act_id FROM actor a, movieActors ma, movie m " +
							"WHERE m.mid=ma.mid AND a.act_id=ma.act_id AND m.title='" + user_movie + "';";  
					myRS = stmt.executeQuery(sql);
					ArrayList<Integer> actorIDs = new ArrayList<>();
					if (myRS.next() == false) {
						actorIDs.add(0);
					}
					else {
						do {
							int cur = myRS.getInt("a.act_id");
							actorIDs.add(cur);
						} while (myRS.next());
					}
					movie_matrix[3] = actorIDs;
				
					top5 = createSimilarityMatrix(movie_matrix, user_movie, mid, stmt);
					
					
					Map<Movie, Integer> results = new HashMap<>();
					for(Map.Entry<Integer, Integer> e : top5.entrySet()) {
						for(int x = 0; x < ml.size(); x++) {
							Movie m = ml.get(x);
							int one = e.getValue();
							int two = m.getId().getId();
							if(one == two) {
								results.put(m, e.getKey());
								break;
							}
						}
					}
					System.out.println("HERE ARE YOUR RECOMMENDATIONS");
					for(Map.Entry<Movie, Integer> e : results.entrySet()) {
						Movie m = e.getKey();
						String title = m.getTitle().getTitle();
						System.out.println(title);
					}
					System.out.println();
					System.out.println("HERE IS SOME DATA TO BACK UP OUR RECOMMENDATIONS:");
					displayUserMovieData(get_user_movie_data);
					System.out.println();
					System.out.println("DATA FOR THE RECOMMENDED MOVIES!:");
					for(Map.Entry<Movie, Integer> e : results.entrySet()) {
						Movie m = e.getKey();
						int score = e.getValue();
						String title = m.getTitle().getTitle();
						String director = m.getDirector().getDirector_name();
						Double popularity = m.getPopularity().getPopularity();
						String genres = " ";
						List<Genre> g = m.getGenres();
						for(int i = 0; i < g.size(); i++) {
							genres += g.get(i).getName() + ", ";
						}
						String keywords = " ";
						List<Keyword> k = m.getKeywords();
						
						for(int i = 0; i < k.size(); i++) {
							keywords += k.get(i).getName() + ", ";
						}
						
						System.out.println("Title: " + title);
						System.out.println("Director: " + director);
						//System.out.println("Popularity: " + popularity);
						System.out.println("Genres:    " + genres.substring(0, genres.length() - 1));
						System.out.println("Keywords:    " +  keywords.substring(0, keywords.length() - 1));
						System.out.println("Similarity score: " + score);
						System.out.println("-----------------------------------------------------------------------------------------");
					}
					
					
					//calculatePopularity(results);
				}
			

			}
			System.out.println("-----------------------------------------------------------------------------------------");
		}

	}
	
	private static void displayUserMovieData(Movie get_user_movie_data) {
		System.out.println("USER'S MOVIE DATA: ");
		String title = get_user_movie_data.getTitle().getTitle();
		String genres = get_user_movie_data.getGenres().toString();
		String keywords = get_user_movie_data.getKeywords().toString();
		Double popularity = get_user_movie_data.getPopularity().getPopularity();
		String director = get_user_movie_data.getDirector().getDirector_name();
		System.out.println("Title: " + title);
		System.out.println("Director: " + director);
		//System.out.println("Popularity: " + popularity);
		System.out.println("Genres:    " + genres);
		System.out.println("Keywords:    " +  keywords);
		//System.out.println("-----------------------------------------------------------------------------------------");
	}




	private static void calculatePopularity(Map<Movie, Integer> results) {
		
		Double score = null;
		for(Map.Entry<Movie, Integer> e : results.entrySet()) {
			Movie m = e.getKey();
			Double imdb_rating = m.getPopularity().getPopularity();
			Integer vote_count = m.getVote_count().getCount();
			//DOuble
			//score = 
		}
		
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




	private static Map<Integer, Integer> createSimilarityMatrix(ArrayList<Integer>[] user_matrix, String name, int user_mid, Statement stmt) throws SQLException {
		String sql = "";
		ResultSet myRS = null;
		Map<Integer, Integer> top5 = new HashMap<>(); 
		sql = "SELECT mid FROM movie WHERE mid !='" + user_mid + "';";
		myRS = stmt.executeQuery(sql);
		if (myRS.next() == false) {
			System.out.println("---------------------------------------------------------------------------------------------------------------");
			System.out.println(name + " was not found. Try again");
			//System.out.println("---------------------------------------------------------------------------------------------------------------");
		} 
		else {
			TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
			ArrayList<Integer> movie_ids = new ArrayList<>();
			//fill up the movie_ids list with mids
			do {
				int cur = myRS.getInt("mid");
				movie_ids.add(cur);
			} while (myRS.next());
			
			//now that we have all the ids, for each mid, we repeat the process to build a movie matrix
			//like we did to create the user_matrix. But now we do this for every mid
			//once we build the matrix, we iterate through and compare the current matrix with
			//user's movie matrix.  Finally, we add an entry to our map, with <mid, sim_score>
			for(int i = 0; i < movie_ids.size(); i++) {
				ArrayList<Integer>[] movie_matrix = new ArrayList[4];
				int id = movie_ids.get(i);
				//GET GENRES:
				sql = "SELECT g.gid FROM genre g, movieGenres mg, movie m " +
						"WHERE m.mid=mg.mid AND g.gid=mg.gid AND m.mid=" + id + ";";
				myRS = stmt.executeQuery(sql);
				ArrayList<Integer> genreIDs = new ArrayList<>();
				if (myRS.next() == false) {	
					genreIDs.add(0);
				}
				else {
					do {
						int cur = myRS.getInt("g.gid");
						genreIDs.add(cur);
					} while (myRS.next());
				}
				movie_matrix[0] = genreIDs;
				
				//GET KEYWORDS:
				//IF KEYWORD COUNT > 4
//				sql = "SELECT k.kwid FROM keyword k, movieKeywords mk, movie m " +
//						"WHERE m.mid=mk.mid AND k.kwid=mk.kwid AND k.count > 4 AND m.mid=" + id + ";";
				sql = "SELECT k.kwid FROM keyword k, movieKeywords mk, movie m " +
						"WHERE m.mid=mk.mid AND k.kwid=mk.kwid AND m.mid=" + id + ";";
				myRS = stmt.executeQuery(sql);
				ArrayList<Integer> keywordIDs = new ArrayList<>();
				//IF KEYWORD COUNT> 4 is an empty set
				if (myRS.next() == false) {
					keywordIDs.add(0);
//					sql = "SELECT k.kwid FROM keyword k, movieKeywords mk, movie m " +
//							"WHERE m.mid=mk.mid AND k.kwid=mk.kwid AND m.mid=" + id + ";";
//					myRS = stmt.executeQuery(sql);
//					if (myRS.next() == false) {
//						keywordIDs.add(0);
//					}
//					else {
//						do {
//							int cur = myRS.getInt("k.kwid");
//							keywordIDs.add(cur);
//						} while (myRS.next());
//					}
				}
				else {
					do {
						int cur = myRS.getInt("k.kwid");
						keywordIDs.add(cur);
					} while (myRS.next());
				}
				movie_matrix[1] = keywordIDs;
				
				//GET DIRECTORS:
				sql = "SELECT d.dir_id FROM director d, movieDirectors md, movie m " +
						"WHERE m.mid=md.mid AND md.dir_id=d.dir_id AND m.mid=" + id + ";";
				myRS = stmt.executeQuery(sql);
				ArrayList<Integer> directorIDs = new ArrayList<>(); 
				if (myRS.next() == false) {
					directorIDs.add(0);
				}
				else {
					do {
						int cur = myRS.getInt("d.dir_id");
						directorIDs.add(cur);
					} while (myRS.next());
				}
				movie_matrix[2] = directorIDs;
				
				//GET ACTORS:
				sql = "SELECT a.act_id FROM actor a, movieActors ma, movie m " +
						"WHERE m.mid=ma.mid AND a.act_id=ma.act_id AND m.mid=" + id + ";";  
				myRS = stmt.executeQuery(sql);
				ArrayList<Integer> actorIDs = new ArrayList<>();
				if (myRS.next() == false) {
					actorIDs.add(0);
				}
				else {
					do {
						int cur = myRS.getInt("a.act_id");
						actorIDs.add(cur);
					} while (myRS.next());
				}
				movie_matrix[3] = actorIDs;
				
				//THIS IS THE SIMPLE ALGORITHM TO GET THE
				//SIMILARITYSCORES FOR EACH MOVIE 
				int count = 0;
				for(int y = 0; y < user_matrix.length; y++) {
					ArrayList<Integer> user =user_matrix[y];
					ArrayList<Integer> cur_movie = movie_matrix[y];
					for(int z = 0; z < user.size(); z++) {
						if(cur_movie.contains(user.get(z))) {
							count++;
						}
					}
				}
				map.put(count, id);
			}
			for(int a = 0 ; a < 5; a++) {
				Entry<Integer, Integer> m = map.pollLastEntry();
				if(m == null) {
					continue;
				}
				Integer k = m.getKey();
				Integer v = m.getValue();
				top5.put(k, v);
			}
		}
		return top5;
	}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static void fillMovieKeywordsTable(Connection conn, List<Movie> ml, Statement stmt) {
		try {
			String sql = "";
			sql = "INSERT INTO movieKeywords VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling movieKeywords table....");
			for(int i = 0; i < ml.size(); i++) {
				Movie m = ml.get(i);
				Integer mid = m.getId().getId();
				if(mid != null) {
					List<Keyword> kwList = m.getKeywords();
					if(kwList != null) {
						for(int j = 0; j <kwList.size(); j++) {
							String id = kwList.get(j).getId();
							if(id != null) {
								Integer kw_id = Integer.parseInt(id);
								if(kw_id != null) {
									pstmt.setInt(1, mid);
									pstmt.setInt(2, kw_id);
									pstmt.executeUpdate();
									//conn.commit();
								}
							}
						}
					}	
				}
			}
			conn.commit();
			pstmt.close();
			System.out.println("movieKeywords table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private static void fillMovieGenresTable(Connection conn, List<Movie> ml, Statement stmt) {
		try {
			String sql = "";
			sql = "INSERT INTO movieGenres VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling movieGenres table....");
			for(int i = 0; i < ml.size(); i++) {
				Movie m = ml.get(i);
				Integer mid = m.getId().getId();
				if(mid != null) {
					List<Genre> gList = m.getGenres();
					if(gList != null) {
						for(int j = 0; j <gList.size(); j++) {
							String id = gList.get(j).getId();
							if(id != null) {
								Integer g_id = Integer.parseInt(id);
								if(g_id != null) {
									pstmt.setInt(1, mid);
									pstmt.setInt(2, g_id);
									pstmt.executeUpdate();
									//conn.commit();
								}
							}
						}
					}	
				}
			}
			conn.commit();
			pstmt.close();
			System.out.println("MovieGenres table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private static void fillMovieDirectorsTable(Connection conn, List<Movie> ml, Statement stmt) {
		try {
			String sql = "";
			sql = "INSERT INTO movieDirectors VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling movieDirectors table....");
			for(int i = 0; i < ml.size(); i++) {
				Movie m = ml.get(i);
				try {
					Integer mid = m.getId().getId();
					Integer dir_id =m.getDirector().getDirector_id();
					if(mid != null && dir_id != null) {
						pstmt.setInt(1, mid);
						pstmt.setInt(2, dir_id);
						pstmt.executeUpdate();
						//conn.commit();
					}
				} catch(NullPointerException e) {
					continue;
				}
			}
			conn.commit();
			pstmt.close();
			System.out.println("movieDirectors table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private static Map<String, Integer> getActorSet(List<Actor> al) {
		Map<String, Integer> actorsList = new HashMap<>();
		for(int i = 0; i < al.size(); i++) {
			String actName = al.get(i).getActor_name();
			Integer act_id = al.get(i).getActor_id();
			if(actName != null && act_id !=null) {
				actorsList.put(actName, act_id);
			}		
		}
		return actorsList;
	}

	private static void fillmovieActorsTable(Connection conn, List<Movie> ml, Statement stmt) {
		try {
			String sql = "";
			sql = "INSERT INTO movieActors VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling movieActors table....");
			for(int i = 0; i < ml.size(); i++) {
				Movie m = ml.get(i);
				Integer mid = m.getId().getId();
				if(mid != null) {
					List<Actor> temp_cast = m.getCast();
					Map<String, Integer> actors = getActorSet(temp_cast);
					for(Map.Entry<String, Integer> a : actors.entrySet()) {
						Integer act_id = a.getValue();
						if(act_id != null) {
							//System.out.println(m.getTitle().getTitle() + " : movie id = " + mid.toString() + " : act_id = " + act_id.toString());
							pstmt.setInt(1, mid);
							pstmt.setInt(2, act_id);
							pstmt.executeUpdate();
							//conn.commit();
						}
					}	
				}
			}
			conn.commit();
			pstmt.close();
			System.out.println("movieActors table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Map<String, Integer> getKeyWords(List<Movie> ml) {
		Map<String, Integer> kwList = new HashMap<>();
		for(int i = 0; i < ml.size(); i++) {
			ArrayList<Keyword> kws = ml.get(i).getKeywords();
			for(int j = 0; j < kws.size(); j++) {
				String currentKW = kws.get(j).getName();
				String id = kws.get(j).getId();
				if(currentKW != null && id !=null) {
					Integer currentID = Integer.parseInt(id);
					kwList.put(currentKW, currentID);
				}		
			}
		}
		return kwList;
	}

	private static Map<Map<String, Integer>, Integer> countKeyWords(Map<String, Integer> keywordSet, List<Movie> ml) {
		Map<Map<String, Integer>, Integer> counted_keywords = new HashMap<>();
		for(Map.Entry<String, Integer> e : keywordSet.entrySet()) {
			String curKW = e.getKey();
			Integer curID = e.getValue();
			Map<String, Integer> kwMap = new HashMap<>();
			kwMap.put(curKW, curID);
			int count = 0;
			for (int j = 0; j < ml.size(); j++) {
				ArrayList<Keyword> curkw = ml.get(j).getKeywords();
				for(int k = 0; k < curkw.size(); k++) {
					String word = curkw.get(k).getName();
					if(curKW.equals(word)) {
						count++;
					}
				}
			}
			counted_keywords.put(kwMap, count);
		}
		return counted_keywords;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static void fillKeywordTable(Connection conn, List<Movie> ml, Statement stmt) {
		Map<String, Integer> keywordSet= new HashMap<>(); 
		//<<keyword, kwid>, count>
		Map<Map<String, Integer>, Integer> keywords = new HashMap<>();
		System.out.println("Filling keyword table....");

		//tyring something new right her
		keywordSet = getKeyWords(ml);

		keywords = countKeyWords(keywordSet, ml);

		try {
			String sql = "";
			sql = "INSERT INTO keyword VALUES (?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			
			for(Map.Entry<Map<String, Integer>, Integer> kw : keywords.entrySet()) {
				Map<String, Integer> id_and_word = kw.getKey();
				Integer kwid = null;
				String keyword = null;
				Integer count = kw.getValue();
				for(Map.Entry<String, Integer> k: id_and_word.entrySet()) {
					kwid = k.getValue();
					keyword = k.getKey();
				}
				pstmt.setInt(1, kwid);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, count);
				pstmt.executeUpdate();
				//conn.commit();
			}
			conn.commit();
			pstmt.close();
			System.out.println("keyword table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private static Map<String, Integer> getGenres(List<Movie> ml) {
		Map<String, Integer> genList = new HashMap<>();
		for(int i = 0; i < ml.size(); i++) {
			ArrayList<Genre> glist = ml.get(i).getGenres();
			for(int j = 0; j < glist.size(); j++) {
				String cur_gen = glist.get(j).getName();
				String id = glist.get(j).getId();
				if(cur_gen != null && id !=null) {
					Integer currentID = Integer.parseInt(id);
					genList.put(cur_gen, currentID);
				}		
			}
		}
		return genList;
	}

	private static void fillGenreTable(Connection conn, List<Movie> ml, Statement stmt) {
		Map<String, Integer> genres = new HashMap<>(); 
		genres = getGenres(ml);
		try {
			String sql = "";
			sql = "INSERT INTO genre VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling genre table....");

			for(Entry<String, Integer> cur_genre : genres.entrySet()) {
				Integer gen_id = cur_genre.getValue();
				String gen_name = cur_genre.getKey();
				pstmt.setInt(1, gen_id);
				pstmt.setString(2, gen_name);
				pstmt.executeUpdate();
				//conn.commit();
			}
			conn.commit();
			pstmt.close();
			System.out.println("genre table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Integer> getDirectors(List<Movie> ml) {
		Map<String, Integer> dirList = new HashMap<>();
		for(int j = 0; j < ml.size(); j++) {
			try {
				String dirName = ml.get(j).getDirector().getDirector_name();
				Integer dir_id = ml.get(j).getDirector().getDirector_id();
//				System.out.println(dirName + " : " + dir_id + " : " + ml.get(j).getTitle().getTitle());
				if(dirName != null && dir_id !=null) {
					dirList.put(dirName, dir_id);
				}		
			}
			catch (NullPointerException ex) {
				continue;
			}	
		}
		return dirList;
	}

	private static void fillDirectorTable(Connection conn, List<Movie> ml, Statement stmt) {
		Map<String, Integer> directors = new HashMap<>(); 
		directors = getDirectors(ml);
		try {
			String sql = "";
			sql = "INSERT INTO director VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling director table....");

			for(Entry<String, Integer> cur_dir : directors.entrySet()) {
				Integer dir_id = cur_dir.getValue();
				String dir_name = cur_dir.getKey();
				pstmt.setInt(1, dir_id);
				pstmt.setString(2, dir_name);
				pstmt.executeUpdate();
				//conn.commit();
			}
			conn.commit();
			pstmt.close();
			System.out.println("actor director complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Integer> getActors(List<Movie> ml) {
		Map<String, Integer> actorsList = new HashMap<>();
		for(int i = 0; i < ml.size(); i++) {
			List<Actor> cast = ml.get(i).getCast();
			for(int j = 0; j < cast.size(); j++) {
				String actName = cast.get(j).getActor_name();
				Integer act_id = cast.get(j).getActor_id();
				if(actName != null && act_id !=null) {
					actorsList.put(actName, act_id);
				}		
			}
		}
		return actorsList;
	}

	//NEED TO MAKE A MAP OF ACTORS AND THEIR CORRESPONDING ID (SET BASICALLY)
	private static void fillActorTable(Connection conn, List<Movie> ml, Statement stmt) {
		Map<String, Integer> actors = new HashMap<>(); 
		actors = getActors(ml);
		try {
			String sql = "";
			sql = "INSERT INTO actor VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling actor table....");

			for(Entry<String, Integer> cur_actor : actors.entrySet()) {

				Integer act_id = cur_actor.getValue();
				String act_name = cur_actor.getKey();
				if (!act_name.contains("\\")) {
					pstmt.setInt(1, act_id);
					pstmt.setString(2, act_name);
					pstmt.executeUpdate();
					//conn.commit();
				}
			}
			conn.commit();
			pstmt.close();
			System.out.println("actor table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void fillMovieTable(Connection conn, List<Movie> ml, Statement stmt) {
		try {
			String sql = "";
			sql = "INSERT INTO movie VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			System.out.println("Filling movie table....");
			for(int i = 0; i < ml.size(); i++) {
				Movie m = ml.get(i);
				Integer mid = m.getId().getId();
				String raw_date = m.getRelease_date().getDate();
				Date release_date = null;
				if (raw_date != null) {
					release_date = Date.valueOf(raw_date);
				}
				String title = m.getTitle().getTitle();
				Double popularity = m.getPopularity().getPopularity();
				Double vote_avg = m.getVote_average().getAverage();
				Integer vote_count =m.getVote_count().getCount();
				pstmt.setInt(1, mid);
				pstmt.setDate(2, release_date);
				pstmt.setString(3, title);
				pstmt.setDouble(4, popularity);
				pstmt.setDouble(5, vote_avg);
				pstmt.setInt(6, vote_count);
				pstmt.executeUpdate();
				//conn.commit();
			}
			conn.commit();
			pstmt.close();
			System.out.println("Movie table complete!");
		}
		catch(SQLException se) {
			System.out.println("SQL Exception.");
			se.printStackTrace();
			try	{
				if(conn!=null)
					conn.rollback();
			}
			catch(SQLException se2)	{
				se2.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}






	

}
