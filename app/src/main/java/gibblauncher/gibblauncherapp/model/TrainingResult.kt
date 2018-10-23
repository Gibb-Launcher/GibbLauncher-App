package gibblauncher.gibblauncherapp.model

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class TrainingResult : RealmObject() {

    var title: String? = null
    var bouncesX: RealmList<Float> = RealmList()
    var bouncesY: RealmList<Float> = RealmList()
    var dateTrainingResult : Date? = null

}