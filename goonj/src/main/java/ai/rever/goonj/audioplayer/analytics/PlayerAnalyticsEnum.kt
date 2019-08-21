package ai.rever.goonj.audioplayer.analytics

import ai.rever.goonj.BuildConfig
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

var isLoggable = false

fun logEvent(tag: String = "unknown_tag", message: String) {
    if (BuildConfig.DEBUG) {
        //Log.e(tag, message)
    }
}


private fun logAnalyticsEvent(message : String?, error : Boolean ?= false){
    val TAG = "ANALYTICS"
    if(error == true){
        Log.e(TAG,"=======error: $message")
    } else if(isLoggable){
        message?.let {
            Log.d(TAG,message)
        }
    }
}

enum class PlayerAnalyticsEnum{
    ON_PLAYER_STATE_CHANGED,
    ON_SEEK_PROCESSED,
    ON_PLAYER_ERROR,
    ON_SEEK_STARTED,
    ON_LOADING_CHANGED,
    ON_VOLUME_CHANGED,
    ON_LOAD_COMPLETED,
    ON_METADATA,
    ON_PLAYBACK_PARAMETERS_CHANGED,
    ON_TRACKS_CHANGED,
    ON_POSITION_DISCONTINUITY,
    ON_REPEAT_MODE_CHANGED,
    ON_SHUFFLE_MODE_ENABLED_CHANGED,
    ON_TIMELINE_CHANGED
}

const val EVENT_TIME = "AnalyticsListener.EventTime"
const val ERROR = "ExoPlaybackException"
const val IS_LOADING = "IsLoading"
const val VOLUME = "Volume"
const val LOAD_EVENT_INFO = "MediaSourceEventListener.LoadEventInfo"
const val MEDIA_LOAD_EVENT = "MediaSourceEventListener.MediaLoadData"
const val METADATA = "Metadata"
const val PLAY_WHEN_READY = "PlayWhenReady"
const val PLAYBACK_STATE = "PlaybackState"
const val ON_PLAYBACK_PARAMETERS_CHANGED = "onPlaybackParametersChanged"
const val PLAYBACK_PARAMETERS = "PlaybackParameters"
const val TRACK_GROUPS = "TrackGroups"
const val TRACK_SELECTIONS ="TrackSelections"
const val REASON = "Reason"
const val REPEAT_MODE = "RepeatMode"
const val SHUFFLE_MODE_ENABLED = "ShuffleModeEnabled"
const val TIMELINE = "Timeline"
const val MANIFEST = "Manifest"

data class AnalyticsModel(
    val type : PlayerAnalyticsEnum,
    val parameter: Map<String, Any?>
){
    override fun toString() : String{
        return "${type.name}  $parameter"
    }

}

private val analyticsSubjectBehaviour : BehaviorSubject<AnalyticsModel> = BehaviorSubject.create()
/**
 * analyticsObservable doesn't allows pushing values
 */
val analyticsObservable get() = analyticsSubjectBehaviour as Observable<AnalyticsModel>

val analyticsObserver = object : Observer<AnalyticsModel> {
    override fun onComplete() {
        logAnalyticsEvent("onComplete")
    }

    override fun onSubscribe(d: Disposable) {
        logAnalyticsEvent("onSubscribe")
    }

    override fun onNext(t: AnalyticsModel) {
        logAnalyticsEvent(t.toString())
    }

    override fun onError(e: Throwable) {
        logAnalyticsEvent(e.message, true)
    }
}

fun logEventBehaviour(behaviour: PlayerAnalyticsEnum, data: Map<String,Any?>){
    analyticsSubjectBehaviour.onNext(AnalyticsModel(behaviour, data))
}


