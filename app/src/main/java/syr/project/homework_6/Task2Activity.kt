package syr.project.homework_6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_task2.*

class Task2Activity : AppCompatActivity(),RecyclerViewFragment.OnRecyclerInteractionListener {
    var movie: MovieData? =null
    var posterid: Int? =null
    private var mTwoPane = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task2)
        setSupportActionBar(toolbarTask2)

        if(savedInstanceState==null){
            val appBar = supportActionBar
            appBar!!.title = null
            toolBarTitle!!.text="Movie List"

            supportFragmentManager.beginTransaction().replace(R.id.fragment1,RecyclerViewFragment()).commit()
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