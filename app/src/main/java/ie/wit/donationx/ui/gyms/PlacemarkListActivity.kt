package org.wit.placemark.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.donationx.R
import ie.wit.donationx.adapters.PlacemarkAdapter
import ie.wit.donationx.adapters.PlacemarkListener
import ie.wit.donationx.databinding.ActivityPlacemarkListBinding
import ie.wit.donationx.main.DonationXApp
import org.wit.gym.activities.GymActivity
import org.wit.gym.models.GymModel

class PlacemarkListActivity : AppCompatActivity(), PlacemarkListener {

    lateinit var app: DonationXApp
    private lateinit var binding: ActivityPlacemarkListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacemarkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as DonationXApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PlacemarkAdapter(app.gyms.findAll().toMutableList(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gymActivityFragment -> {
                val launcherIntent = Intent(this, GymActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

     private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.placemarks.findAll().size)
            }
        }

    override fun onPlacemarkClick(placemark: GymModel) {
        val launcherIntent = Intent(this, GymActivity::class.java)
        launcherIntent.putExtra("gym_edit", placemark)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.placemarks.findAll().size)
            }
        }
}