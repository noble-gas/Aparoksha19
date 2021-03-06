package org.aparoksha.app19.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_events.*
import org.aparoksha.app19.R
import org.aparoksha.app19.activities.MainActivity
import org.aparoksha.app19.adapters.CategoryAdapter
import org.aparoksha.app19.models.Event
import org.aparoksha.app19.viewmodels.EventsViewModel


class EventsFragment : Fragment() {
    private val TAG = EventsFragment::class.java.simpleName

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    private lateinit var adapter: CategoryAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.events_fragment_title)

        categoryRecyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)

        val toolBar = (activity as MainActivity).toolBar
        categoryRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            val value = categoryRecyclerView.canScrollVertically(-1)
            if (!value) {
                toolBar.elevation = 0f
            } else {
                toolBar.elevation = 16f
            }
        }

        adapter = CategoryAdapter()
        categoryRecyclerView.adapter = adapter

        val allEvents = ViewModelProviders.of(this.activity!!).get(EventsViewModel::class.java)
        allEvents.getData(false)
        allEvents.allEvents.observe(this, Observer {
            it?.let {
                val list = ArrayList<Event>()
                for (element in it)
                    list.add(element)

                adapter.updateEvents(list)
            }
        })
    }
}
