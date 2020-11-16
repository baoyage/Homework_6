package syr.project.homework_6

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {
    companion object {
        private val DB_NAME = "mymovies.db"
        private val DB_VER = 1
        private val COL_VOTE_CNT = "vote_count"
        private val COL_ID = "id"
        private val COL_VOTE_AVG = "vote_average"
        private val COL_TITLE = "title"
        private val COL_POPULARITY = "popularity"
        private val COL_POSTER = "poster_path"
        private val COL_ORG_LANG = "original_language"
        private val COL_ORG_TITLE = "original_title"
        private val COL_BACKDROP = "backdrop_path"
        private val COL_OVERVIEW = "overview"
        private val COL_RELEASE = "release_date"
        private val COL_STARS="stars"
//        private val COL_CHECKED="checked"
//        private val COL_GENRE = "genre_name"
        private val COL_MOVIE_ID = "movie_id"
//        private val COL_GENRE_ID = "genre_id"

        // create table Movies
        private val CREATE_TABLE_MOVIES = "CREATE TABLE IF NOT EXISTS movies " +
         "( $COL_ID INTEGER PRIMARY KEY, $COL_MOVIE_ID INTEGER, $COL_TITLE TEXT, " +
         "$COL_VOTE_AVG REAL, $COL_VOTE_CNT INTEGER, $COL_POSTER TEXT, $COL_BACKDROP TEXT, " +
         "$COL_POPULARITY REAL, $COL_OVERVIEW TEXT, $COL_ORG_LANG TEXT, $COL_ORG_TITLE TEXT, " +
         "$COL_RELEASE TEXT,  $COL_STARS TEXT )"
//        private val CREATE_TABLE_GENRE = "CREATE TABLE IF NOT EXISTS genres ( $COL_ID INTEGER PRIMARY KEY, $COL_GENRE TEXT )"
//        private val CREATE_TABLE_MOVIE_GENRES = "CREATE TABLE IF NOT EXISTS movie_genres ( $COL_ID INTEGER PRIMARY KEY" +
//                 ",$COL_MOVIE_ID INTEGER, $COL_GENRE_ID INTEGER )"
        private val DROP_TABLE_MOVIES = "DROP TABLE IF EXISTS movies"
//        private val DROP_TABLE_GENRES = "DROP TABLE IF EXISTS genres"
//        private val DROP_TABLE_MOVIE_GENRES = "DROP TABLE IF EXISTS movie_genres"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_TABLE_MOVIES)
//        db!!.execSQL(CREATE_TABLE_GENRE)
//        db!!.execSQL(CREATE_TABLE_MOVIE_GENRES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_TABLE_MOVIES)
//        db!!.execSQL(DROP_TABLE_GENRES)
//        db!!.execSQL(DROP_TABLE_MOVIE_GENRES)
    }
    fun closeDB(){
        val db = this.readableDatabase
        if(db != null && db.isOpen)
            db.close()
    }
    fun initializeTables(){
        val db = this.readableDatabase
        var query = "SELECT * FROM movies"
        var c = db.rawQuery(query, null)
        if( c.count <= 0) {
            insertAllMovies()
        }

    }

    fun insertAllMovies(){
        val myStaticMovie = MovieList()
        for(movie in myStaticMovie.movieList){
            val id = addMovie(movie)
        }
    }

    fun addMovie(movie: MovieData): Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_MOVIE_ID, movie.id)
        values.put(COL_VOTE_AVG, movie.vote_average)
        values.put(COL_TITLE, movie.title)
        values.put(COL_ORG_TITLE, movie.original_title)
        values.put(COL_ORG_LANG, movie.original_language)
        values.put(COL_OVERVIEW, movie.overview)
        values.put(COL_POPULARITY, movie.popularity)
        values.put(COL_POSTER, movie.poster_path)
        values.put(COL_BACKDROP, movie.backdrop_path)
        values.put(COL_VOTE_CNT, movie.vote_count)
        values.put(COL_RELEASE, movie.release_date)
        values.put(COL_STARS, movie.stars)
        return db.insert("movies", null, values)
    }
//    fun addGenre(type: Int, genre: String): Long{
//        val db = this.writableDatabase
//        val values = ContentValues()
//        values.put(COL_ID, type)
//        values.put(COL_GENRE, genre)
//        return db.insert("genres", null, values)
//    }
    fun getMovie(mid: Int): MovieData? {
        val query = "SELECT * FROM movies WHERE $COL_ID = $mid"
        val db = this.readableDatabase
        val c = db.rawQuery(query, null)
        if (c.moveToFirst()){
            val movie = MovieData(
                vote_count = c.getInt(c.getColumnIndex(COL_VOTE_CNT)),
                id = c.getInt(c.getColumnIndex(COL_MOVIE_ID)),
                vote_average = c.getDouble(c.getColumnIndex(COL_VOTE_AVG)),
                title = c.getString(c.getColumnIndex(COL_TITLE)),
                popularity = c.getDouble(c.getColumnIndex(COL_POPULARITY)),
                poster_path = c.getString(c.getColumnIndex(COL_POSTER)),
                original_language = c.getString(c.getColumnIndex(COL_ORG_LANG)),
                original_title =  c.getString(c.getColumnIndex(COL_ORG_TITLE)),
                backdrop_path = c.getString(c.getColumnIndex(COL_BACKDROP)),
                overview = c.getString(c.getColumnIndex(COL_OVERVIEW)),
                release_date = c.getString(c.getColumnIndex(COL_RELEASE)),
                stars = c.getString(c.getColumnIndex(COL_STARS)),
                db_id = c.getInt(c.getColumnIndex(COL_ID))
            )


            return movie
        }
        else
            return null
    }
    fun getAllMovies(): ArrayList<MovieData> {
        val movies=ArrayList<MovieData>()
        var vote_countt=0
        val query = "SELECT * FROM movies"
        var db=this.readableDatabase
        val c = db.rawQuery(query, null)
        if (c.moveToFirst()){
            do{
                val movie = MovieData(
                        vote_count = c.getInt(c.getColumnIndex(COL_VOTE_CNT)),

                        id = c.getInt(c.getColumnIndex(COL_MOVIE_ID)),
                        vote_average = c.getDouble(c.getColumnIndex(COL_VOTE_AVG)),
                        title = c.getString(c.getColumnIndex(COL_TITLE)),
                        popularity = c.getDouble(c.getColumnIndex(COL_POPULARITY)),
                        poster_path = c.getString(c.getColumnIndex(COL_POSTER)),
                        original_language = c.getString(c.getColumnIndex(COL_ORG_LANG)),
                        original_title = c.getString(c.getColumnIndex(COL_ORG_TITLE)),
                        backdrop_path = c.getString(c.getColumnIndex(COL_BACKDROP)),
                        overview = c.getString(c.getColumnIndex(COL_OVERVIEW)),
                        release_date = c.getString(c.getColumnIndex(COL_RELEASE)),
                        stars = c.getString(c.getColumnIndex(COL_STARS)),
                        db_id = c.getInt(c.getColumnIndex(COL_ID))
                )



                movies.add(movie)
            }while(c.moveToNext())
        }
        return movies




    }

    fun deleteMovie(movie: MovieData){
        val db = this.writableDatabase
//        var d ="$COL_MOVIE_ID in"+"(SELECT $COL_MOVIE_ID = ? from movies order by COL_ID limit 1)"

//        var movieid= movie.id.toString()

//        val query = "SELECT * FROM movies WHERE $COL_MOVIE_ID = $movieid"
//        val c = db.rawQuery(query, null)
//        if (c.moveToFirst()){
//            val movie = MovieData(
//                    vote_count = c.getInt(c.getColumnIndex(COL_VOTE_CNT)),
//                    id = c.getInt(c.getColumnIndex(COL_MOVIE_ID)),
//                    vote_average = c.getDouble(c.getColumnIndex(COL_VOTE_AVG)),
//                    title = c.getString(c.getColumnIndex(COL_TITLE)),
//                    popularity = c.getDouble(c.getColumnIndex(COL_POPULARITY)),
//                    poster_path = c.getString(c.getColumnIndex(COL_POSTER)),
//                    original_language = c.getString(c.getColumnIndex(COL_ORG_LANG)),
//                    original_title =  c.getString(c.getColumnIndex(COL_ORG_TITLE)),
//                    backdrop_path = c.getString(c.getColumnIndex(COL_BACKDROP)),
//                    overview = c.getString(c.getColumnIndex(COL_OVERVIEW)),
//                    release_date = c.getString(c.getColumnIndex(COL_RELEASE)),
//                    stars = c.getString(c.getColumnIndex(COL_STARS)),
//                    db_id = c.getInt(c.getColumnIndex(COL_ID))
//            )
//
//
//
//        }

        db.delete("movies","$COL_ID = ?", arrayOf(movie.db_id.toString()))


//        db.execSQL("DELETE from movies WHERE $COL_MOVIE_ID in (SELECT $COL_MOVIE_ID from movies WHERE $COL_MOVIE_ID = $movieid LIMIT(1)) ")
//        db.execSQL("SET ROWCOUNT 1"+
//                "DELETE from movies WHERE $COL_MOVIE_ID in (SELECT $COL_MOVIE_ID from movies WHERE $COL_MOVIE_ID = $movieid )")

//        db.delete("movies","$COL_MOVIE_ID = ?", arrayOf(movie.id.toString()))
//        db.delete("movies","$COL_MOVIE_ID in"+"(SELECT $COL_MOVIE_ID = ? " +
//                "from movies order by COL_ID limit 1)", arrayOf(movie.id.toString()))
        db.close()
    }
//    fun updateMovie(movie: MovieData): Int{
//        val db = this.writableDatabase
//        val values = ContentValues()
//        values.put(COL_MOVIE_ID, movie.id)
//        values.put(COL_VOTE_AVG, movie.vote_average)
//        values.put(COL_TITLE, movie.title)
//        values.put(COL_ORG_TITLE, movie.original_title)
//        values.put(COL_ORG_LANG, movie.original_language)
//        values.put(COL_OVERVIEW, movie.overview)
//        values.put(COL_POPULARITY, movie.popularity)
//        values.put(COL_POSTER, movie.poster_path)
//        values.put(COL_BACKDROP, movie.backdrop_path)
//        values.put(COL_VOTE_CNT, movie.vote_count)
//        values.put(COL_RELEASE, movie.release_date)
//        return db.update("movies", values, "COL_ID = ?"
//            , arrayOf(movie.db_id.toString()))
//    }







}
