package gibblauncher.gibblauncherapp.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gibblauncher.gibblauncherapp.R
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem


class MainActivity : AppCompatActivity() {
    val fragment1: Fragment = ConnectFragment()
    val fragment2: Fragment = PlayFragment()
    val fragment3: Fragment = HawkeyeResultFragment()
    val fm = supportFragmentManager
    var active = fragment2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    return true
                }

                R.id.action_play -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    return true
                }

                R.id.action_history -> {
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                    return true
                }
            }
            return false
        }
    }

}
