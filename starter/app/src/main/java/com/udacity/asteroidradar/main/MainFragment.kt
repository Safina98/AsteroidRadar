package com.udacity.asteroidradar.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.DateFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val my_view: FrameLayout = binding.activityMainImageOfTheDayLayout
            my_view.visibility = View.GONE
        }
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter =MainAdapter(MainAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })
        viewModel.selected_item.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigatedToDetail()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       viewModel.updateFilter(
           when (item.itemId) {
               R.id.today_asteroids -> DateFilter.SHOW_TODAY
               R.id.weeks_asteroid -> DateFilter.SHOW_WEEK
               else -> DateFilter.SHOW_ALL
           }
       )
        return true
    }
}
