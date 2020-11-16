package syr.project.homework_6


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_list_item_left.view.*
import java.util.*
import kotlin.collections.ArrayList


class MyMovieListAdapter(context: Context
) : RecyclerView.Adapter<MyMovieListAdapter.MovieViewHolder>() {
    var myListener:MyItemClickListener? = null
    var lastPosition=-1

    val myDB = DatabaseHelper(context)
    lateinit var movieList: ArrayList<MovieData>
    var posterTable:MutableMap<String, Int> = mutableMapOf()
    init {
        myDB.initializeTables()

        movieList = myDB.getAllMovies()
//        posterTable[movieList[0].title]=R.drawable.pci1aryw7oj2eyto2nmyekhhicp
//        posterTable[movieList[1].title]=R.drawable.oyg9tl7fcrp4ez9vid6ukzwdndz
//        posterTable[movieList[2].title]=R.drawable.riaoojrfvvhotyaogoi0wr7okse
//        posterTable[movieList[3].title]=R.drawable.zgvbrulkupqpbwginedkjpyqum4
//        posterTable[movieList[4].title]=R.drawable.t7xhp8sqtvanzecvn1oqrvwcxti
//        posterTable[movieList[5].title]=R.drawable.q719jxxezooyaps6babgknononx
//        posterTable[movieList[6].title]=R.drawable.hek3koduyrqk7fihpxsa6mt2zc3
//        posterTable[movieList[7].title]=R.drawable.h1b7tw0t399vdjacwjh8m87469b
//        posterTable[movieList[8].title]=R.drawable.velwphvmqeqkcxggneu8ymio52r
//        posterTable[movieList[9].title]=R.drawable.plnlrtbuult0rh3xsjmpubiso3l
    }






    lateinit var movie:MovieData
    var posterid: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View
        view=when(viewType){
            1 -> {
                layoutInflater.inflate(R.layout.movie_list_item_right,parent,false)
            }
            2 -> {
                layoutInflater.inflate(R.layout.movie_list_item_left,parent,false)
            }
            else->{
                layoutInflater.inflate(R.layout.movie_list_item_right,parent,false)
            }
        }

        return MovieViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        if(movieList[position].vote_average >8){
            return 1
        }
        else{
            return 2
        }
    }
    inner class MovieViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        val rVMovieTitle= view?.rVTitle
        val rVOverview= view?.rVOverview
        val rVposterid= view?.rVPosterid
        val rVRating= view?.rVRating
        val rVCheckBox= view?.rVCheckBox
        val overflow = view?.overflow
        init{
            overflow?.setOnClickListener {
                if(myListener != null){
                    if(adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION){
                        myListener!!.onOverflowMenuClickedFromAdapter(it, adapterPosition)
                    }
                }
            }

            rVCheckBox?.setOnCheckedChangeListener { rVCheckBox,isChecked->
                movieList[this.adapterPosition].checked =isChecked

            }
            view?.setOnClickListener {
                if(myListener!=null){
                    if(adapterPosition!=NO_POSITION){

//                        myListener!!.onItemClickedFromAdapter(movieList[adapterPosition],posterTable[movieList[adapterPosition].title])
                    }
                }
            }
            view?.setOnLongClickListener {
                if(myListener!=null){
                    if(adapterPosition!=NO_POSITION){

                        myListener!!.onItemLongClickedFromAdapter(adapterPosition)
                    }
                }
                true
            }
        }



    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie=movieList[position]
        holder.rVMovieTitle!!.text =movie.title
        holder.rVOverview!!.text=movie.overview
//        holder.rVposterid!!.setImageResource(posterTable[movie.title]!!)

        val url = "https://image.tmdb.org/t/p/w185/" + movie.poster_path!!

        val picasso = Picasso.Builder(holder.itemView.context).listener { _, _, e -> e.printStackTrace() }.build()
        picasso.load(url).into(holder.rVposterid)
        Picasso.get().load(url).error(R.mipmap.ic_launcher).into(holder.rVposterid)
        holder.rVRating!!.text= movie.vote_average.toString()
        holder.rVCheckBox!!.isChecked= movie.checked!!
//        setAnimation(holder.itemView, position)


    }
    fun setAnimation(view:View,position:Int){
        if(position!=lastPosition){
            val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)
            animation.duration = 700
            animation.startOffset = position * 100L
            view.startAnimation(animation)
        }
        lastPosition = position
    }

    override fun getItemCount(): Int {
        return movieList.size
    }


    fun setMyItemClickListener(listener: MyItemClickListener) {
        this.myListener=listener
    }

    interface MyItemClickListener {
        fun onItemClickedFromAdapter(movie:MovieData, posterid: Int?)
        fun onItemLongClickedFromAdapter(position : Int)
        fun onOverflowMenuClickedFromAdapter(it: View?, adapterPosition: Int) {

        }

    }
    fun setSelectAll() {
        for( i in movieList.indices){
            movieList[i].checked = true
            notifyItemChanged(i)
        }
//        notifyDataSetChanged()

    }
    fun setClearAll() {
        for( i in movieList.indices){
            movieList[i].checked = false
            notifyItemChanged(i)
        }
//        notifyDataSetChanged()
    }
    fun deleteMovies() {
        var cnt = 0
        for(i in 0 until movieList.size)
            if(movieList[i].checked!!)
                cnt += 1
        for(i in 0 until cnt){
            for(j in movieList.indices){
                if(movieList[j].checked!!){
//                    posterTable.remove(movieList[j].title)
                    movieList.removeAt(j)


//                    movieList.removeAt(j)
                    notifyItemRemoved(j)
                    break
                }
            }
        }
//        notifyDataSetChanged()
//        notifyItemRemoved()
    }
    fun deleteMovies(position: Int){
        var movie=movieList[position].copy()
        myDB.deleteMovie(movie)
        movieList.removeAt(position)
        notifyItemRemoved(position)

    }
    fun duplicateMovie(position: Int){


        var movie=movieList[position].copy()
        myDB.addMovie(movie)
        movieList.add(position+1,movie)
        notifyItemInserted(position+1)
//        notifyDataSetChanged()

    }
    fun findFirst(query: String): Int {
        for(i in movieList.indices){
            if(movieList[i].title.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
                return i
        }
        return -1
    }

    fun getItem(position: Int): Any {
        return movieList[position ]

    }
    fun sortItemsByTitle(){
        var mL=movieList.sortedBy { it.title }
        movieList=ArrayList(mL)
        notifyDataSetChanged()
    }
    fun sortItemsByRating(){
        var mL=movieList.sortedBy { it.vote_average }
        movieList=ArrayList(mL)
        notifyDataSetChanged()
    }





}




