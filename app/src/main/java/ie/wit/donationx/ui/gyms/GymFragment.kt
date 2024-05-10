package ie.wit.donationx.ui.gyms

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.donationx.R
import ie.wit.donationx.adapters.PlacemarkAdapter
import ie.wit.donationx.adapters.PlacemarkListener
import ie.wit.donationx.databinding.ActivityPlacemarkListBinding
import ie.wit.donationx.main.DonationXApp
import org.wit.gym.activities.GymActivity
import org.wit.gym.fragments.GymMapsFragment
import org.wit.gym.models.GymCloudStore
import org.wit.gym.models.GymModel
import org.wit.placemark.models.PlacemarkModel

class GymFragment : Fragment(), PlacemarkListener {

    companion object {
        const val ARG_PLACEMARKS = "arg_placemarks"

        fun newInstance(placemarks: List<PlacemarkModel>): GymFragment {
            val fragment = GymFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_PLACEMARKS, ArrayList(placemarks))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: ActivityPlacemarkListBinding
    lateinit var app: DonationXApp

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private lateinit var cloud: GymCloudStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPlacemarkListBinding.inflate(inflater, container, false)
        app = requireActivity().application as DonationXApp
        setupMenu()
        binding.recyclerView.adapter = PlacemarkAdapter(app.gyms.findAll(), this)
        val layoutManager = LinearLayoutManager(this.requireActivity())
        binding.recyclerView.layoutManager = layoutManager
        // Get placemarks from arguments

        // binding.toolbar.title = title
        //setSupportActionBar(binding.toolbar)
        cloud = GymCloudStore(this.requireActivity())


            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = PlacemarkAdapter(app.gyms.findAll(), this)

            Toast.makeText(requireContext(), "No placemarks available", Toast.LENGTH_SHORT).show()


        return binding.root
    }


    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.gyms.findAll().size)
            }
        }


    override fun onPlacemarkClick(placemark: GymModel) {
        val launcherIntent = Intent(requireContext(), GymActivity::class.java)
        launcherIntent.putExtra("gym_edit", placemark)
        getClickResult.launch(launcherIntent)
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
               return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gymActivityFragment -> {
                val launcherIntent = Intent(requireContext(), GymActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.GymMapsFragment -> {
                val launcherIntent = Intent(requireContext(), GymMapsFragment::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.gyms.findAll().size)
            }
        }

    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}