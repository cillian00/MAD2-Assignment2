package ie.wit.donationx.ui.gyms

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.donationx.R
import ie.wit.donationx.databinding.ActivityPlacemarkBinding
import ie.wit.donationx.main.DonationXApp
import ie.wit.donationx.utils.showImagePicker
import org.wit.gym.models.GymModel
import org.wit.gym.models.Location
import org.wit.placemark.activities.MapActivity
import timber.log.Timber

class GymActivityFragment : Fragment() {

    private lateinit var binding: ActivityPlacemarkBinding
    private var gym = GymModel()
    private lateinit var app: DonationXApp
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private var edit = false;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPlacemarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        app = requireActivity().application as DonationXApp

        if (requireActivity().intent.hasExtra("gym_edit")) {
            edit = true
            gym = requireActivity().intent.extras?.getParcelable("gym_edit")!!
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

        val numberPicker = view.findViewById<NumberPicker>(R.id.members)
        numberPicker.minValue = 1
        numberPicker.maxValue = 100
        numberPicker.value = 0

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Toast.makeText(requireContext(), "Selected Number: $newVal", Toast.LENGTH_SHORT).show()
        }

        binding.btnAdd.setOnClickListener {
            gym.title = binding.gymTitle.text.toString()
            gym.description = binding.description.text.toString()
            gym.contactNumber = binding.contactNumber.text.toString()
            gym.members = binding.members.value
            gym.activity = binding.activity.text.toString()
            if (gym.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_gym_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.gyms.update(gym.copy())
                } else {
                    app.gyms.create(gym.copy())
                }
            }
            Timber.i("add Button Pressed: $gym")
            requireActivity().setResult(RESULT_OK)
            requireActivity().finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, requireActivity())
        }

        binding.gymLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (gym.zoom != 0f) {
                location.lat = gym.lat
                location.lng = gym.lng
                location.zoom = gym.zoom
            }
            val launcherIntent = Intent(requireContext(), MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.btnDelete.setOnClickListener {
            app.gyms.delete(gym.copy())
            Timber.i("Gym ID: ${gym.copy().id}")
            requireActivity().finish()
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_gym, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                requireActivity().finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            val image = result.data!!.data!!
                            requireActivity().contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            gym.image = image
                            Picasso.get()
                                .load(gym.image)
                                .into(binding.gymImage)
                            binding.chooseImage.setText(R.string.change_gym_image)
                        }
                    }
                    RESULT_CANCELED -> { }
                    else -> { }
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
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            gym.lat = location.lat
                            gym.lng = location.lng
                            gym.zoom = location.zoom
                        }
                    }

                    RESULT_CANCELED -> { }
                    else -> { }
                }
            }
    }
}
