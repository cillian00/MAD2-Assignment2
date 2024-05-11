package ie.wit.donationx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.donationx.databinding.CardGymBinding
import org.wit.gym.models.GymModel

interface GymClickListener {
    fun onGymClick(gym: GymModel)
}

class GymAdapter(private var gyms: ArrayList<GymModel>, private val listener: GymClickListener, private val readOnly: Boolean)
    : RecyclerView.Adapter<GymAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGymBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding, readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val gym = gyms[position]
        holder.bind(gym, listener)
    }


    override fun getItemCount(): Int = gyms.size

    inner class MainHolder(val binding: CardGymBinding, private val readOnly: Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(placemark: GymModel, listener: GymClickListener) {
            binding.placemarkTitle.text = placemark.title
            binding.description.text = placemark.description
            binding.numberOfMembers.text = "We currently have: " + placemark.members + " members!"
            binding.gymStatus.text = "We are currently: " + placemark.activity
            binding.contactNumber.text = "Contact us at: " + placemark.contactNumber

            Picasso.get().load(placemark.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onGymClick(placemark) }
        }
    }
}
