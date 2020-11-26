package com.example.travelapp.fragments.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelapp.R
import com.example.travelapp.data.User
import com.example.travelapp.data.UserViewModel
import com.example.travelapp.fragments.add.AddFragment
import com.example.travelapp.fragments.add.AddFragmentArgs
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.trips_item.*
import kotlinx.android.synthetic.main.trips_item.view.*
import java.lang.Exception


class ListFragment : Fragment(),ListAdapter.OnItemClickListener {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_list, container, false)
        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }


            //userViewModel
            mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
            mUserViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->

                val adapter = ListAdapter(user,this)
                val recyclerView = view.recyclerView
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                adapter.setData(user)
                //Return the number of trips created to the textView number_of_trips
                // add "s" to the trip if it is more than one trip
                 number_of_trips.text = if (user.size==1 || user.isEmpty()) "${user.size} Trip" else "${user.size} Trips"


            })

            return view
        }

    override fun showMenu(view: View, user: User) {
        val popupMenu = PopupMenu(view.context,view)
        popupMenu.inflate(R.menu.trip_edition)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete_trip ->{
                        mUserViewModel.deleteUser(user)
                        Toast.makeText(requireContext(),"Successfully removed: ${user.departure} to {user.destination}"+" Trip?",Toast.LENGTH_SHORT).show()
                    }
                R.id.update_trip ->{
                val action = ListFragmentDirections.actionListFragmentToAddFragment(user)
                    view.findNavController().navigate(action)
                }
                R.id.delete_all_trip ->{
                mUserViewModel.deleteAllUser()
                }
            }
            true
        }
    }
}
