package ie.wit.donationx.main

import android.app.Application
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.database.database
import ie.wit.donationx.models.PlacemarkJSONStore
import org.wit.gym.models.GymCloudStore
import org.wit.gym.models.GymStore
import org.wit.placemark.models.PlacemarkModel
import org.wit.placemark.models.PlacemarkStore
import timber.log.Timber

class DonationXApp : Application() {

    //lateinit var donationsStore: DonationStore
    lateinit var placemarks: PlacemarkStore
    lateinit var gyms: GymStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        placemarks = PlacemarkJSONStore(applicationContext)
        gyms = GymCloudStore(applicationContext)
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
        Timber.i("DonationX Application Started")
    }
}