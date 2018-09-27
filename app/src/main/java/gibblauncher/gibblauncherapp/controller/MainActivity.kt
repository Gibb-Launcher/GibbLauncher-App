package gibblauncher.gibblauncherapp.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import gibblauncher.gibblauncherapp.R
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import io.realm.Realm
import io.realm.RealmConfiguration




class MainActivity : AppCompatActivity() {
    val fragment1: Fragment = StatisticFragment()
    val fragment2: Fragment = PlayFragment()
    val fragment3 = HawkeyeResultFragment()
    val fm = supportFragmentManager
    var active = fragment2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startRealm()

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").commit()
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").hide(fragment1).commit()

    }


    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_connect -> {
                    if(active is StatisticFragment){
                        return true
                    }
                    fm.beginTransaction()
                            .setCustomAnimations( R.anim.left_in, R.anim.right_out)
                            .hide(active)
                            .show(fragment1)
                            .commit()
                    active = fragment1
                    return true
                }

                R.id.action_play -> {
                    if(active is PlayFragment){
                        return true
                    }
                    val inPosition: Int
                    val outPosition: Int

                    if(active is StatisticFragment){
                        inPosition =  R.anim.right_in
                        outPosition = R.anim.left_out
                    }else {
                        inPosition =  R.anim.left_in
                        outPosition = R.anim.right_out
                    }

                    fm.beginTransaction()
                            .setCustomAnimations(inPosition, outPosition)
                            .hide(active)
                            .show(fragment2)
                            .commit()
                    active = fragment2
                    return true
                }

                R.id.action_history -> {
                    if(active is HawkeyeResultFragment){
                        return true
                    }
                    fm.beginTransaction()
                            .setCustomAnimations( R.anim.right_in, R.anim.left_out)
                            .hide(active)
                            .show(fragment3)
                            .commit()
                    active = fragment3
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        fragment3.displayBalls()
                    },1000)

                    return true
                }
            }
            return false
        }
    }

    private fun startRealm() {
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .name("gibblauncher.realm")
                .schemaVersion(0)
                .build()
        Realm.setDefaultConfiguration(realmConfig)

    }
}
