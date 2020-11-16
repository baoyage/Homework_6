package syr.project.homework_6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_task2.toolBarTitle
import kotlinx.android.synthetic.main.activity_task3.*

class Task3Activity : AppCompatActivity(),RecyclerViewTask3Fragment.OnRecyclerInteractionListener {
    var movie: MovieData? =null
    var posterid: Int? =null
    private var mTwoPane = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task3)
        setSupportActionBar(toolbarTask3)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)


        if(savedInstanceState==null){
            val appBar = supportActionBar
            appBar!!.title = null
            toolBarTitle!!.text="Movie List"

            supportFragmentManager.beginTransaction().replace(R.id.fragment1,RecyclerViewTask3Fragment()).commit()
        }





    }

    override fun onItemClicked(movie:MovieData, posterid: Int?) {
        val appBar = supportActionBar
        toolBarTitle!!.text = movie.title
        supportFragmentManager.beginTransaction().replace(R.id.fragment1,DetailFragment.newInstance(movie,
            posterid!!
        )).addToBackStack(null).commit()
    }
}