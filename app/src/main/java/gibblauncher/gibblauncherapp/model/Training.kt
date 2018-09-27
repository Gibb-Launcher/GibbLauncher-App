package gibblauncher.gibblauncherapp.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Required


open class Training : RealmObject() {

    @Required
    private var title: String? = null
    private val shots: RealmList<String>? = null

}