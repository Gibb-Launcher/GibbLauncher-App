package gibblauncher.gibblauncherapp.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class TrainingResult : RealmObject() {

    var title: String? = null
    var bouncesX: RealmList<Float> = RealmList()
    var bouncesY: RealmList<Float> = RealmList()

}