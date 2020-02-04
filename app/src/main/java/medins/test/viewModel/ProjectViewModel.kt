package medins.test.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import medins.test.R
import medins.test.api.model.Project
import medins.test.api.repository.ApiClientRepository
import medins.test.utils.PreferenceManager
import java.net.UnknownHostException

class ProjectViewModel (
    private val myApplication: Application,
    private val mProjectID: String
) : AndroidViewModel(myApplication) {

    private val repository = ApiClientRepository.instance
    private val preferenceManager: PreferenceManager = PreferenceManager(myApplication)
    val projectLiveData: MutableLiveData<Pair<Project?, Int>> = MutableLiveData()

    var project = ObservableField<Project>()

    init {
        loadProject()
    }

    private fun loadProject() {
        viewModelScope.launch {

            try {
                val project = repository.getProjectDetails(preferenceManager.githubUserName, mProjectID)
                if (project.isSuccessful) {
                    projectLiveData.postValue(Pair(project.body()!!, 200))
                } else {
                    projectLiveData.postValue(Pair(null, -1))
                }
            } catch (eU: UnknownHostException) {
                projectLiveData.postValue(Pair(null, 0))
                eU.stackTrace
            } catch (e: Exception) {
                projectLiveData.postValue(Pair(null, -1))
                Log.e("loadProject:Failed", e.stackTrace.toString())
            }
        }
    }

    fun setProject(project: Project) {
        this.project.set(project)
    }

    class Factory(private val application: Application, private val projectID: String) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProjectViewModel(application, projectID) as T
        }
    }
}