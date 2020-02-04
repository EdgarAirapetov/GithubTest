package medins.test.ui.fragments

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import medins.test.R
import medins.test.databinding.FragmentUsernameBinding
import medins.test.utils.PreferenceManager

const val TAG_OF_USERNAME_FRAGMENT = "UsernameFragment"

class UsernameFragment : Fragment() {

    private lateinit var binding: FragmentUsernameBinding
    private var preferenceManager: PreferenceManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_username, container, false)
        context?.let {
            preferenceManager = PreferenceManager(it)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            getUsername.setOnClickListener {
                val username = usernameInput.text.toString()
                if (username.isEmpty()) {
                    Toast.makeText(activity, R.string.error_input_username_empty, Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                preferenceManager?.githubUserName = username

                openProjectListFragment()


            }
        }
    }

    private fun openProjectListFragment() {
        val fragment = ProjectListFragment()
        activity?.supportFragmentManager?.beginTransaction()?.addToBackStack("projects")?.replace(
            R.id.fragment_container, fragment,
            TAG_OF_PROJECT_LIST_FRAGMENT
        )?.commit()
    }
}