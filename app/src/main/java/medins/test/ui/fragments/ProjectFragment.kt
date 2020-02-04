package medins.test.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import medins.test.R
import medins.test.databinding.FragmentProjectDetailsBinding
import medins.test.viewModel.ProjectViewModel
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider


private const val KEY_PROJECT_ID = "project_id"

class ProjectFragment  : Fragment() {

    private lateinit var binding: FragmentProjectDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_details, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val projectID = arguments?.getString(KEY_PROJECT_ID)

        val factory = ProjectViewModel.Factory(
            requireActivity().application, projectID ?: ""
        )

        val viewModel = ViewModelProvider(this, factory).get(ProjectViewModel::class.java)

        binding.apply {
            projectViewModel = viewModel
            isLoading = true
        }

        observeViewModel(viewModel)

    }

    private fun observeViewModel(viewModel: ProjectViewModel) {
        viewModel.projectLiveData.observe(viewLifecycleOwner, Observer { pair ->
            binding.isLoading = false
            if (pair.first != null) {
                binding.projectLayout.visibility = VISIBLE
                viewModel.apply {
                    setProject(pair.first!!)

                    binding.copyUrl.setOnClickListener {
                        copyToClipBoard(project.get()?.clone_url)
                    }
                }

            } else {
                binding.errorTv.visibility = VISIBLE
                when (pair.second) {
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

    private fun copyToClipBoard(text: String?) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("label", text)
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(activity, R.string.copied_url, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun forProject(projectID: String): ProjectFragment {
            val fragment = ProjectFragment()
            val args = Bundle()

            args.putString(KEY_PROJECT_ID, projectID)
            fragment.arguments = args

            return fragment
        }
    }
}