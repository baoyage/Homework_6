package syr.project.homework_6


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_task2.*
import kotlinx.android.synthetic.main.fragment_detail.*
import java.io.Serializable
import syr.project.homework_6.Task2Activity as Task2Activity

private const val ARG_MOV1 = "movie"
private const val ARG_MOV2 = "posterid"
class DetailFragment : Fragment() {

    private var movie: MovieData? = null
    private var posterid:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        setHasOptionsMenu(true)
        arguments?.let {
            movie = it.getSerializable(ARG_MOV1) as MovieData
            posterid = it.getInt(ARG_MOV2)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieTitle.text=movie!!.title
        release_date.text=movie!!.release_date
        stars.text=movie!!.stars
        ratingBar.rating= movie!!.vote_average.toFloat()/2
        overview.text=movie!!.overview
//        poster.setImageResource(posterid)
        val url = "https://image.tmdb.org/t/p/w185/" + movie!!.poster_path!!

//        val picasso = Picasso.Builder(holder.itemView.context).listener { _, _, e -> e.printStackTrace() }.build()
//        picasso.load(url).into(holder.rVposterid)
//        Picasso.get().load(url).error(R.mipmap.ic_launcher).into(holder.rVposterid)



    }

    override fun onDestroy() {
        super.onDestroy()
//        activity!!.title="Movie List"

    }
    override fun onDetach() {
        super.onDetach()
//        (activity as Task2Activity).supportActionBar?.title = "Movie List"
//        (activity as Task2Activity)
//        activity!!.title="Movie List"
        if(activity?.javaClass?.simpleName=="Task2Activity"){
            (activity as Task2Activity).toolBarTitle!!.text = "Movie List"
        }
        else{
            (activity as Task3Activity).toolBarTitle!!.text = "Movie List"
        }





    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        if( menu?.findItem(R.id.action_search) == null)
            inflater.inflate(R.menu.toolbar_menu_task2_detail, menu)
        val shareItem = menu!!.findItem(R.id.action_share)
        val mShareActionProvider = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_TEXT, movie!!.title!!)
        if (mShareActionProvider != null && intentShare != null)
            mShareActionProvider.setShareIntent(intentShare)
        super.onCreateOptionsMenu(menu, inflater)

    }



    companion object {

        @JvmStatic
        fun newInstance(movie: MovieData, posterid: Int) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_MOV1, movie as Serializable)
                    putInt(ARG_MOV2, posterid)
                }
            }
    }
}