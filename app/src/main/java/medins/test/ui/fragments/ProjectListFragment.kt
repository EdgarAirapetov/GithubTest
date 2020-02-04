package medins.test.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_project_list.view.*
import medins.test.MainActivity
import medins.test.R
import medins.test.api.model.Project
import medins.test.databinding.FragmentProjectListBinding
import medins.test.ui.adapter.ProjectAdapter
import medins.test.ui.callback.ProjectClickCallback
import medins.test.utils.PreferenceManager
import medins.test.viewModel.ProjectListViewModel

const val TAG_OF_PROJECT_LIST_FRAGMENT = "ProjectListFragment"

class ProjectListFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(ProjectListViewModel::class.java)
    }

    private lateinit var binding: FragmentProjectListBinding
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var preferenceManager: PreferenceManager

    private val projectClickCallback = object : ProjectClickCallback {
        override fun onClick(project: Project) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is MainActivity) {
                (activity as MainActivity).show(project)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)

        projectAdapter = ProjectAdapter(projectClickCallback)

        context?.let {
            preferenceManager = PreferenceManager(it)
        }


        binding.apply {
            projectList.adapter = projectAdapter
            isLoading = true
        }

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: ProjectListViewModel) {
        viewModel.projectListLiveData.observe(viewLifecycleOwner, Observer { pair ->
            binding.isLoading = false
            if (pair.first != null) {
                if (pair.first!!.isNotEmpty()) {
                    binding.recyclerLayout.visibility = VISIBLE
                    activity?.let {
                        binding.projects.text = String.format(it.getString(R.string.github_projects), preferenceManager.githubUserName)
                    }
                    projectAdapter.setProjectList(pair.first!!)
                } else {
                    binding.errorTv.visibility = VISIBLE
                    binding.errorTv.setText(R.string.error_no_projects)
                }
            } else {
                binding.root.error_tv.visibility = VISIBLE
                when (pair.second) {
                    404 -> {
                        binding.errorTv.setText(R.string.error_not_found_user)
                    }
                    0 -> {
                        binding.errorTv.setText(R.string.error_no_internet)
                    }
                    -1 -> {
                        binding.errorTv.setText(R.string.error_unknown)
                    }
                }
            }
        })
    }
}