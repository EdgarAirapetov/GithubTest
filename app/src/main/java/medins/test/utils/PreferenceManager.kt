package medins.test.utils

import android.content.Context

const val GITHUB_USERNAME = "GITHUB_USERNAME"

class PreferenceManager (context: Context) {

    private val PRIVATE_MODE = 0
    private val pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor = pref.edit()

    var githubUserName: String
        get() {
            return pref.getString(GITHUB_USERNAME, "").toString()
        }
        set(userName) {
            editor.putString(GITHUB_USERNAME, userName)
            editor.commit()
        }

    companion object {
        const val PREF_NAME = "MEDINS_TEST"
    }

}