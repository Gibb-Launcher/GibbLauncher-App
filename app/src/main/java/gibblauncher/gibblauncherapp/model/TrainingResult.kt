package gibblauncher.gibblauncherapp.model

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class TrainingResult : RealmObject() {

    var id: Int = 0
    var title: String? = null
    var shots: RealmList<String> = RealmList()
    var bouncesLocations: RealmList<BounceLocation> = RealmList()
    var dateTrainingResult : Date? = null

}