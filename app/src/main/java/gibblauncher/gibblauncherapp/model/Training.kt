package gibblauncher.gibblauncherapp.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required


open class Training : RealmObject() {

    @Required
    @PrimaryKey
    var title: String? = null
    var shots: RealmList<String> = RealmList()

}