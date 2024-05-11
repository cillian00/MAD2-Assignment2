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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.donationx.R
import ie.wit.donationx.adapters.PlacemarkAdapter
import ie.wit.donationx.adapters.PlacemarkListener
import ie.wit.donationx.databinding.ActivityPlacemarkListBinding
import ie.wit.donationx.main.DonationXApp
import ie.wit.donationx.utils.SwipeToDeleteCallback
import org.wit.gym.activities.GymActivity
import org.wit.gym.fragments.GymMapsFragment
import org.wit.gym.models.GymCloudStore
import org.wit.gym.models.GymModel
import timber.log.Timber

class GymFragment : Fragment(), PlacemarkListener {

    interface ThemeChangeListener {
        fun onThemeChanged(isDarkMode: Boolean)
    }

    private lateinit var binding: ActivityPlacemarkListBinding
    lateinit var app: DonationXApp

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private lateinit var cloud: GymCloudStore
    private var isNightModeOn = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPlacemarkListBinding.inflate(inflater, container, false)
        app = requireActivity().application as DonationXApp
        setupMenu()
        binding.recyclerView.adapter = PlacemarkAdapter(app.gyms.findAll().toMutableList(), this)
        val layoutManager = LinearLayoutManager(this.requireActivity())
        binding.recyclerView.layoutManager = layoutManager
        // Get placemarks from arguments

        // binding.toolbar.title = title
        //setSupportActionBar(binding.toolbar)
        cloud = GymCloudStore(this.requireActivity())


            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = PlacemarkAdapter(app.gyms.findAll().toMutableList(), this)

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
            R.id.item_filter -> {
                true
            }
            R.id.gymActivityFragment -> {
                val launcherIntent = Intent(requireContext(), GymActivity::class.java)
                getResult.launch(launcherIntent)
                return true
            }
            R.id.GymMapsFragment -> {
                val launcherIntent = Intent(requireContext(), GymMapsFragment::class.java)
                mapIntentLauncher.launch(launcherIntent)
                return true
            }
            R.id.theme_change -> {
                (activity as? ThemeChangeListener)?.onThemeChanged(isNightModeOn)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as PlacemarkAdapter
                val position = viewHolder.adapterPosition
                val deletedGym = adapter.gyms[position]
                adapter.removeAt(position)
                app.gyms.delete(deletedGym)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}
class YourActivity : AppCompatActivity(), GymFragment.ThemeChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_DonationX)
        setContentView(R.layout.activity_gym)

    }

    override fun onThemeChanged(isDarkMode: Boolean) {
        if (isDarkMode) {
            setTheme(R.style.Theme_DonationX_Dark)
        } else {
            setTheme(R.style.Theme_DonationX)
        }
        recreate()
    }
}
