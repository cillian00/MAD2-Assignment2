package ie.wit.donationx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.copy
import com.squareup.picasso.Picasso
import ie.wit.donationx.databinding.CardGymBinding
import ie.wit.donationx.main.DonationXApp
import org.wit.gym.models.GymModel

interface PlacemarkListener {
    fun onPlacemarkClick(placemark: GymModel)
}



class PlacemarkAdapter constructor(
    var gyms: MutableList<GymModel>,
    private val listener: PlacemarkListener) :
        RecyclerView.Adapter<PlacemarkAdapter.MainHolder>() {
    lateinit var app: DonationXApp

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGymBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = gyms[holder.adapterPosition]
        holder.bind(placemark, listener)
    }

    override fun getItemCount(): Int = gyms.size
    fun removeAt(position: Int) {
        gyms.removeAt(position)
        notifyItemRemoved(position)
    }


    class MainHolder(private val binding : CardGymBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(placemark: GymModel, listener: PlacemarkListener) {
            binding.placemarkTitle.text = placemark.title
            binding.description.text = placemark.description
            binding.numberOfMembers.text = "We currently have: " + placemark.members + " members!"
            binding.gymStatus.text = "We are currently: " + placemark.activity
            binding.contactNumber.text = "Contact us at: " + placemark.contactNumber

            Picasso.get().load(placemark.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onPlacemarkClick(placemark) }
        }
    }
}
