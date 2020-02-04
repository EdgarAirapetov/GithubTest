package medins.test.ui.callback

import medins.test.api.model.Project

interface ProjectClickCallback {
    fun onClick(project: Project)
}