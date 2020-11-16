package syr.project.homework_6

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator


import kotlinx.android.synthetic.main.fragment_recycler_view.*

class RecyclerViewFragment() : Fragment(),
    MyMovieListAdapter.MyItemClickListener{

    private var listener: OnRecyclerInteractionListener? = null
    lateinit var myAdapter:MyMovieListAdapter
    var mL=ArrayList(MovieList().movieList)


    override fun onItemClickedFromAdapter(movie: MovieData, posterid: Int?) {
        onItemClickedFromRecyclerViewFragment(movie,posterid)
    }

    override fun onItemLongClickedFromAdapter(position: Int) {
//        myAdapter.duplicateMovie(position)
        activity!!.startActionMode(ActionBarCallBack(position))


    }

    inner class ActionBarCallBack( val position: Int) : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode!!.menuInflater.inflate(R.menu.menu_popup, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val movie = myAdapter.getItem(position) as MovieData

            mode!!.title = movie.title
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when(item!!.itemId){
                R.id.action_dup -> {
                    myAdapter.duplicateMovie(position)
                    mode!!.finish()
                }
                R.id.action_rem -> {
                    myAdapter.deleteMovies(position)
                    mode!!.finish()
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {

        }

    }


    interface OnRecyclerInteractionListener {
        fun onItemClicked(movie: MovieData,posterid: Int?)

    }
    fun onItemClickedFromRecyclerViewFragment(movie: MovieData,posterid: Int?) {
        listener?.onItemClicked(movie,posterid)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        setHasOptionsMenu(true)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
//        toolBarTitle!!.text="Movie List"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myAdapter= MyMovieListAdapter(view.context)
        recyclerView.layoutManager= GridLayoutManager(context,1)
//        val myAdapter= MyMovieListAdapter(movieList ,posterTable)
        myAdapter.setMyItemClickListener(this)
        recyclerView.adapter=myAdapter
        val alphaAdapter = AlphaInAnimationAdapter(myAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter).apply {
            // Change the durations.
            setDuration(1000)
            // Change the interpolator.
            setInterpolator(OvershootInterpolator())
            // Disable the first scroll mode.
            setFirstOnly(false)


        }
        recyclerView.itemAnimator = SlideInLeftAnimator(OvershootInterpolator()).apply {

            addDuration = 1000
            removeDuration = 100
            moveDuration = 1000
            changeDuration = 100

        }


        myAdapter.sortItemsByTitle()
        selectAll.setOnClickListener {
            myAdapter.setSelectAll()
//            myAdapter.notifyDataSetChanged()
//            recyclerView.swapAdapter(myAdapter,true)
//            recyclerView.scrollBy(0,0)
        }
        clearAll.setOnClickListener {
            myAdapter.setClearAll()
//            myAdapter.notifyDataSetChanged()

        }
        delete.setOnClickListener {
            myAdapter.deleteMovies()
//            myAdapter.notifyDataSetChanged()

        }
//        toolBarTitle!!.text="Movie List"
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRecyclerInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnRecyclerInteractionListener")
        }
//        toolBarTitle!!.text="Movie List"
    }

    override fun onResume() {
        super.onResume()
//        toolBarTitle!!.text="Movie List"
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if(activity?.javaClass?.simpleName=="Task2Activity"){
            if( menu?.findItem(R.id.action_search) == null)
                inflater.inflate(R.menu.toolbar_menu_task2, menu)
            val search = menu?.findItem(R.id.action_search)!!.actionView as SearchView
            if ( search != null ){
                search.setOnQueryTextListener( object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {

                        val pos = myAdapter.findFirst( query!! )
                        if (pos >= 0) {
                            recyclerView.smoothScrollToPosition(pos)
                            Toast.makeText(context, "Search Movie " + query + " found ... ", Toast.LENGTH_LONG).show()
                        } else {
                            recyclerView.smoothScrollToPosition(0)
                            Toast.makeText(context, " not found ... ", Toast.LENGTH_LONG).show()
                        }
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }

            val expandListener = object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    // Do something when action item collapses
                    return true // Return true to collapse action view
                }
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    // Do something when expanded
                    return true // Return true to expand action view
                }
            }
            // Get the MenuItem for the action item
            val actionMenuItem = menu?.findItem(R.id.action_search)


            actionMenuItem?.setOnActionExpandListener(expandListener)
        }
        else{
            if( menu?.findItem(R.id.action_search) == null)
                inflater.inflate(R.menu.toolbar_menu_task3_top, menu)



        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item!!.itemId){
//            R.id.action_alphabet ->{
//                MovieList.sort
//            }
//        }
        when(item!!.itemId){
            R.id.action_alphabet ->
                myAdapter.sortItemsByTitle()







//             mL.sortedBy { it.title } as ArrayList<MovieData>
//            else -> ArrayList(MovieList().movieList)
        }
        myAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("RestrictedApi")
    override fun onOverflowMenuClickedFromAdapter(view: View?, position: Int) {
        val popup = PopupMenu(context!!, view!!)
        val menuInflater = popup.menuInflater
        menuInflater.inflate(R.menu.menu_popup, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_dup -> {

                    myAdapter.duplicateMovie(position)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_rem -> {
                    myAdapter.deleteMovies(position)
                    return@setOnMenuItemClickListener true
                }
                else ->{
                    return@setOnMenuItemClickListener false
                }
            }
        }
        // show icon on the popup menu!!
        val menuHelper = MenuPopupHelper(this.context!!, popup.menu as MenuBuilder, view)
        menuHelper.setForceShowIcon(true)
        menuHelper.gravity = Gravity.END
        menuHelper.show()
    }


//    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            RecyclerViewFragment(movieList, posterTable).apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//    }
}


