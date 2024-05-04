package com.example.cryptocurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.cryptocurrency.databinding.ActivityMainBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {items ->
            when(items.itemId){
                R.id.home-> setfragment(home_())
                R.id.setting->setfragment(settings())
                R.id.person->setfragment(Profile())
                else -> {

                }

            }
            true
        }
        setfragment(home_())

    }
    public fun setfragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout2,fragment).commit()
    }
}