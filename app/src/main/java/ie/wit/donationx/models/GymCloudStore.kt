package org.wit.gym.models

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber
import kotlin.random.Random


class GymCloudStore(context: Context) : GymStore {

    var gyms = mutableListOf<GymModel>()

    init {
        findAll()
    }
    private fun generateRandomId(): Long {
        return Random.nextLong(1, 10001)
    }


    override fun findAll(): MutableList<GymModel> {
        val ref = FirebaseDatabase.getInstance().getReference("Gym")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gyms.clear()
                for (productSnapshot in dataSnapshot.children) {
                    val gymStorage = productSnapshot.getValue(GymModelStorage::class.java)
                    if (gymStorage != null) {
                        val gym = GymModel(
                            id = gymStorage.id,
                            title = gymStorage.title,
                            members = gymStorage.members,
                            contactNumber = gymStorage.contactNumber,
                            activity = gymStorage.activity,
                            description = gymStorage.description,
                            image = Uri.parse(gymStorage.image),
                            lat = gymStorage.lat,
                            lng = gymStorage.lng,
                            zoom = gymStorage.zoom
                        )
                        gyms.add(gym)
                    }
                }
                gyms.forEach { Timber.i("$it") }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
        return gyms
    }

    override fun findAll(userid: String, gymModels: MutableLiveData<List<GymModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(gymModels: MutableLiveData<List<GymModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAllFiltered(filter: String, callback: (List<GymModel>) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Gym")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val filteredGyms = mutableListOf<GymModel>()
                for (productSnapshot in dataSnapshot.children) {
                    val gymStorage = productSnapshot.getValue(GymModelStorage::class.java)
                    if (gymStorage != null && gymStorage.title.contains(filter, ignoreCase = true)) {
                        val gym = GymModel(
                            id = gymStorage.id,
                            title = gymStorage.title,
                            description = gymStorage.description,
                            members = gymStorage.members,
                            contactNumber = gymStorage.contactNumber,
                            activity = gymStorage.activity,
                            image = Uri.parse(gymStorage.image),
                            lat = gymStorage.lat,
                            lng = gymStorage.lng,
                            zoom = gymStorage.zoom
                        )
                        filteredGyms.add(gym)
                    }
                }
                callback(filteredGyms)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }



    override fun create(gym: GymModel) {

        gym.id = generateRandomId().toString()
        Timber.tag("CreateGym").i("Generated ID: %s", gym.id)

        val database = FirebaseDatabase.getInstance()
        val baseRef = database.getReference("Gym").child("${gym.id}")
        Timber.tag("CreateGym").i("Using ID for Firebase path: %s", gym.id)

        baseRef.child("id").setValue(gym.id)
            .addOnSuccessListener {
                Timber.tag("Added id").i("id has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add id").i("id has NOT been added.")
            }

        baseRef.child("title").setValue(gym.title)
            .addOnSuccessListener {
                Timber.tag("Added Title").i("Title has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add Title").i("Title has NOT been added.")
            }

        baseRef.child("description").setValue(gym.description)
            .addOnSuccessListener {
                Timber.tag("Added Description").i("Description has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add Description").i("Description has NOT been added.")
            }


        baseRef.child("contactNumber").setValue(gym.contactNumber)
            .addOnSuccessListener {
                Timber.tag("Added contactNumber").i("contactNumber has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add contactNumber").i("contactNumber has NOT been added.")
            }



        baseRef.child("members").setValue(gym.members)
            .addOnSuccessListener {
                Timber.tag("Added members").i("members has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add members").i("members has NOT been added.")
            }



        baseRef.child("activity").setValue(gym.activity)
            .addOnSuccessListener {
                Timber.tag("Added activity").i("activity has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add activity").i("activity has NOT been added.")
            }


        baseRef.child("image").setValue(gym.image.toString())
            .addOnSuccessListener {
                Timber.tag("Added image").i("image has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add image").i("image has NOT been added.")
            }

        baseRef.child("lat").setValue(gym.lat)
            .addOnSuccessListener {
                Timber.tag("Added Lateral").i("Lateral has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add Lateral").i("Lateral has NOT been added.")
            }

        baseRef.child("lng").setValue(gym.lng)
            .addOnSuccessListener {
                Timber.tag("Added Longitude").i("Longitude has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add Longitude").i("Longitude has NOT been added.")
            }

        baseRef.child("zoom").setValue(gym.zoom)
            .addOnSuccessListener {
                Timber.tag("Added zoom").i("zoom has been added.")
            }
            .addOnFailureListener {
                Timber.tag("Failed to add zoom").i("zoom has NOT been added.")
            }

        gyms.add(gym)
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, gyms: GymModel) {
        TODO("Not yet implemented")
    }

    override fun delete(gym: GymModel) {

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Gym").child(gym.id.toString())

        ref.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                gyms.remove(gym)
            } else {
                task.exception?.let {
                    throw it
                }
            }
        }

    }

    override fun delete(userid: String, gymId: String) {
        TODO("Not yet implemented")
    }


    override fun update(gym: GymModel) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Gym").child(gym.id.toString())

        val gymUpdates = hashMapOf<String, Any>(
            "title" to gym.title,
            "description" to gym.description,
            "members" to gym.members,
            "contactNumber" to gym.contactNumber,
            "activity" to gym.activity,
            "image" to gym.image.toString(),
            "lat" to gym.lat,
            "lng" to gym.lng,
            "zoom" to gym.zoom
        )

        ref.updateChildren(gymUpdates).addOnSuccessListener {
            Timber.tag("UpdateGym").i("Gym updated successfully: ${gym.id}")
        }.addOnFailureListener { exception ->
            Timber.tag("UpdateGym").e(exception, "Failed to update Gym: ${gym.id}")
        }
    }

    override fun update(userid: String, gymId: String, donation: GymModel) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, donationid: String, donation: MutableLiveData<GymModel>) {
        TODO("Not yet implemented")
    }


}
