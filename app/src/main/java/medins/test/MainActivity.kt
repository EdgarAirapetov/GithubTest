package medins.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import medins.test.api.model.Project
import medins.test.ui.fragments.ProjectFragment
import medins.test.ui.fragments.ProjectListFragment
import medins.test.ui.fragments.TAG_OF_USERNAME_FRAGMENT
import medins.test.ui.fragments.UsernameFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = UsernameFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment,
                    TAG_OF_USERNAME_FRAGMENT
                )
                .commit()
        }
    }

    fun show(project: Project) {
        val projectFragment = ProjectFragment.forProject(project.name)

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("project")
            .replace(R.id.fragment_container, projectFragment, null)
            .commit()
    }
}
