package com.jdevs.timeo.ui.activitydetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.jdevs.timeo.R
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.ActivitydetailFragBinding
import com.jdevs.timeo.ui.activities.RecordDialog
import com.jdevs.timeo.ui.graphs.GraphsItemFragment
import com.jdevs.timeo.ui.graphs.GraphsPagerAdapter
import com.jdevs.timeo.util.observeEvent
import javax.inject.Inject

class ActivityDetailFragment : ActionBarFragment() {

    override val menuId = R.menu.activity_detail_fragment_menu
    private val args: ActivityDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: ActivityDetailViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity!!.application as TimeoApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.setActivity(args.activity)
        viewModel.setupActivityLiveData(args.activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ActivitydetailFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewModel = viewModel

            it.graphsViewpager.adapter = ActivityDetailGraphsAdapter(this) {

                findNavController().navigate(R.id.action_activityDetailFragment_to_graphsFragment)
            }

            TabLayoutMediator(it.graphsTablayout, it.graphsViewpager) { tab, position ->

                it.graphsViewpager.setCurrentItem(tab.position, true)

                tab.text = when (position) {
                    0 -> "Day"
                    1 -> "Week"
                    else -> "Month"
                }
            }.attach()
        }

        viewModel.apply {

            activity.observe(viewLifecycleOwner) { activity ->

                setActivity(activity)
            }

            observeEvent(showRecordDialog) {

                RecordDialog(context!!) { time ->

                    addRecord(activity.value!!, time)
                }.show()
            }
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.editActivity) {

            val directions =
                ActivityDetailFragmentDirections.actionActivityDetailFragmentToAddEditActivityFragment(
                    activity = viewModel.activity.value,
                    isEdited = true
                )

            findNavController().navigate(directions)

            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }

    private class ActivityDetailGraphsAdapter(
        fragment: Fragment,
        private val onClick: (View) -> Unit = {}
    ) : GraphsPagerAdapter(fragment) {

        override fun createFragment(position: Int) = GraphsItemFragment(onClick)
    }
}
