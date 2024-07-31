package com.example.myapplication.ui.viewhistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class GalleryFragment : Fragment() {

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Create RecyclerView programmatically
        val recyclerView = RecyclerView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        rootLayout.addView(recyclerView)

        // Fetch data from Firestore
        fetchLocationData { locations ->
            val adapter = LocationAdapter(locations)
            recyclerView.adapter = adapter
        }

        return rootLayout
    }

    private fun fetchLocationData(callback: (List<LocationData>) -> Unit) {
        db.collection("locations")
            .get()
            .addOnSuccessListener { result ->
                val locations = mutableListOf<LocationData>()
                for (document in result) {
                    val latitude = document.getDouble("latitude") ?: 0.0
                    val longitude = document.getDouble("longitude") ?: 0.0
                    locations.add(LocationData(latitude, longitude))
                }
                callback(locations)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    companion object {
        private const val TAG = "GalleryFragment"
    }
}

// Data class for location data
data class LocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

// Adapter for RecyclerView
class LocationAdapter(private val locations: List<LocationData>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }
        return LocationViewHolder(textView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int = locations.size

    class LocationViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(location: LocationData) {
            textView.text = "Lat: ${location.latitude}, Lon: ${location.longitude}"
        }
    }
}
