package org.wit.gym.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.donationx.R
import ie.wit.donationx.databinding.ActivityPlacemarkBinding
import ie.wit.donationx.main.DonationXApp
import ie.wit.donationx.utils.showImagePicker
import org.wit.gym.models.GymModel
import org.wit.gym.models.Location
import org.wit.placemark.activities.MapActivity
import timber.log.Timber.Forest.i

class GymActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    var gym = GymModel()
    lateinit var app: DonationXApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)


        app = application as DonationXApp

        i("gym Activity started...")

        if (intent.hasExtra("gym_edit")) {
            edit = true
            gym = intent.extras?.getParcelable("gym_edit")!!
            binding.gymTitle.setText(gym.title)
            binding.description.setText(gym.description)
            binding.btnAdd.setText(R.string.save_gym)
            Picasso.get()
                .load(gym.image)
                .into(binding.gymImage)
            if (gym.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_gym_image)
            }

        }

        val numberPicker = findViewById<NumberPicker>(R.id.members)
        numberPicker.minValue = 1
        numberPicker.maxValue = 100
        numberPicker.value = 0

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Toast.makeText(this, "Selected Number: $newVal", Toast.LENGTH_SHORT).show()
        }

        binding.btnAdd.setOnClickListener() {
            gym.title = binding.gymTitle.text.toString()
            gym.description = binding.description.text.toString()

            // Assuming `contactNumber` is an EditText
            gym.contactNumber = binding.contactNumber.text.toString()

            gym.members = binding.members.value

            gym.activity = binding.activity.text.toString()
            if (gym.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_gym_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.gyms.update(gym.copy())
                } else {
                    app.gyms.create(gym.copy())
                }
            }
            i("add Button Pressed: $gym")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        binding.gymLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (gym.zoom != 0f) {
                location.lat =  gym.lat
                location.lng = gym.lng
                location.zoom = gym.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.btnDelete.setOnClickListener {
            app.gyms.delete(gym.copy())
            Log.i("Gym ID: ", gym.copy().id.toString())
            finish()
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_gym, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            gym.image = image

                            Picasso.get()
                                .load(gym.image)
                                .into(binding.gymImage)
                            binding.chooseImage.setText(R.string.change_gym_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            gym.lat = location.lat
                            gym.lng = location.lng
                            gym.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }


}