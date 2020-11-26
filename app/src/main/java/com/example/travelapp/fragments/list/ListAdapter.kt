package com.example.travelapp.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.travelapp.R
import com.example.travelapp.data.User
import kotlinx.android.synthetic.main.trips_item.view.*

class ListAdapter(private val user: List<User>,private val listener: OnItemClickListener):RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

   inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private val recyclerMenu: ImageView = itemView.findViewById(R.id.recycler_menu)
       val location: TextView = itemView.findViewById(R.id.location)
       val destinations: TextView =itemView.findViewById(R.id.destinations)
       val takeOfDate: TextView = itemView.findViewById(R.id.take_off_date)
       val arrivalDate: TextView =itemView.findViewById(R.id.arrival_date)
       val takeOffTime: TextView =itemView.findViewById(R.id.take_off_time)
       val arrivalTime: TextView =itemView.findViewById(R.id.arrival_time)
       val recyclerTripType: TextView =itemView.findViewById(R.id.recycler_trip_type)


        init {
            recyclerMenu.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(adapterPosition !=RecyclerView.NO_POSITION){
                listener.showMenu(recyclerMenu,user[adapterPosition])
            }
        }
    }
    interface OnItemClickListener{
        fun showMenu(view: View,user: User)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.trips_item, parent, false))

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = user[position]
        holder.apply {
            location.text = currentUser.departure
            destinations.text = currentUser.destination
            takeOfDate.text = currentUser.departureDate
            arrivalDate.text = currentUser.arrivalDate
            takeOffTime.text = currentUser.departureTime
            arrivalTime.text = currentUser.arrivalTime
            recyclerTripType.apply {
                text = currentUser.tripType

                backgroundTintList = when(text.toString()){
                    "Business" ->
                        ContextCompat.getColorStateList(this.context,R.color.business)
                    "Education" ->
                        ContextCompat.getColorStateList(this.context,R.color.education)
                    "Medical" ->
                        ContextCompat.getColorStateList(this.context,R.color.medical)
                    "Vacation" ->
                        ContextCompat.getColorStateList(this.context,R.color.vacation)
                    else -> ContextCompat.getColorStateList(this.context,R.color.colorPrimaryDark)
                }
            }
        }


    }
    fun setData(user: List<User>) {
        this.userList = user
        notifyDataSetChanged()
    }
}
