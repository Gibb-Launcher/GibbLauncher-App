package gibblauncher.gibblauncherapp.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class TrainingResult : RealmObject() {

    @Required
    @PrimaryKey
    var id: String? = null
    var launcherPosition: Int? = null
    var shots: RealmList<String> = RealmList()
    var isAleatory: Boolean = false
    var possibleShots: RealmList<String> = RealmList()
    var bouncesX: RealmList<Float> = RealmList()
    var bouncesY: RealmList<Float> = RealmList()

}