package medins.test.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import medins.test.R
import medins.test.api.model.Project
import medins.test.api.repository.ApiClientRepository
import medins.test.utils.PreferenceManager
import java.net.UnknownHostException

class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ApiClientRepository.instance
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    var projectListLiveData: MutableLiveData<Pair<List<Project>?, Int>> = MutableLiveData()

    init {
        loadProjectList()
    }

    private fun loadProjectList() {
        viewModelScope.launch {
            try {
                val request =
                    repository.getProjectList(preferenceManager.githubUserName)
                if (request.isSuccessful) {
                    projectListLiveData.postValue(Pair(request.body(), 200))
                } else {
                    projectListLiveData.postValue(Pair(null, -1))
                }
            } catch (eU: UnknownHostException) {
                projectListLiveData.postValue(Pair(null, 0))
                eU.stackTrace
            } catch (e: Exception) {
                projectListLiveData.postValue(Pair(null, -1))
                e.stackTrace
            }
        }
    }
}