package org.teamblueridge.pasteitapp

import android.content.SharedPreferences
import android.util.Log

/**
 * Class to handle the create, recent, and languages URLs for the Stikked API

 * @author Kyle Laker (kalaker)
 * *
 * @version 1.0
 */
class UploadDownloadUrlPrep {
    private var mUrl: String? = null

    /**
     * Gets the proper URL for uploading a paste or for downloading a list of recent pastes (JSON)
     * or a list of languages the server supports (JSON) for syntax highlighting
     *
     * @param prefs  The preferences to be able to access the domain, API key, etc.
     * @param upDown "upCreate" for creating a new paste
     *               "downLangs" for getting languages
     *               "downRecents" for recent pasts
     * @return The URL to be used, with the API key if necessary
     */
    fun prepUrl(prefs: SharedPreferences, upDown: String): String {

        // Ensure that the paste URL is set, if not, default to Team BlueRidge
        val mPasteDomain: String
        val mLangDownloadUrl: String
        val mRecentDownloadUrl: String
        val mUploadUrl: String

        if (prefs.getString("pref_domain", "").length <= 0) {
            mPasteDomain = prefs.getString("pref_domain", "")
        } else {
            mPasteDomain = "https://paste.teamblueridge.org"
        }

        // Only set the API key for Team BlueRidge because we know our key
        if (mPasteDomain == "https://paste.teamblueridge.org") {
            mUploadUrl = mPasteDomain + CREATE_WITH_APIKEY + TEAMBLUERIDGE_APIKEY
            mLangDownloadUrl = mPasteDomain + LANGS_WITH_APIKEY + TEAMBLUERIDGE_APIKEY
            mRecentDownloadUrl = mPasteDomain + RECENT_WITH_APIKEY + TEAMBLUERIDGE_APIKEY
        } else {
            if (prefs.getString("pref_api_key", "").length <= 0) {
                val mPasteApiKey = prefs.getString("pref_api_key", "")
                mUploadUrl = mPasteDomain + CREATE_WITH_APIKEY + mPasteApiKey
                mLangDownloadUrl = mPasteDomain + LANGS_WITH_APIKEY + mPasteApiKey
                mRecentDownloadUrl = mPasteDomain + RECENT_WITH_APIKEY + mPasteApiKey
            } else {
                mUploadUrl = mPasteDomain + "/api/create"
                mLangDownloadUrl = mPasteDomain + "/api/langs"
                mRecentDownloadUrl = mPasteDomain + "/api/recent"
            }
        }

        when (upDown) {
            "upCreate" -> mUrl = mUploadUrl
            "downLangs" -> mUrl = mLangDownloadUrl
            "downRecent" -> mUrl = mRecentDownloadUrl
            else -> Log.e(TAG, "Unknown URL case")
        }

        return mUrl as String
    }

    companion object {
        private val TAG = "TeamBlueRidge"
        private val TEAMBLUERIDGE_APIKEY = "teamblueridgepaste"
        private val LANGS_WITH_APIKEY = "/api/langs?apikey="
        private val CREATE_WITH_APIKEY = "/api/create?apikey="
        private val RECENT_WITH_APIKEY = "/api/recent?apikey="
    }
}