package org.wit.gym.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface GymStore {
    fun findAll(): List<GymModel>

    fun findAllFiltered(filter: String, callback: (List<GymModel>) -> Unit)
    fun create(gym: GymModel)
    fun delete(gym: GymModel)
    fun update(gym: GymModel)


    // New Methods:

    fun findAll(userid:String, gymModels:
                MutableLiveData<List<GymModel>>
    )
    fun findAll(gymModels:
    MutableLiveData<List<GymModel>>
    )
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, gyms: GymModel)
    fun delete(userid:String, gymId: String)
    fun update(userid:String, gymId: String, donation: GymModel)

    fun findById(userid:String, donationid: String,
                 donation: MutableLiveData<GymModel>)
}