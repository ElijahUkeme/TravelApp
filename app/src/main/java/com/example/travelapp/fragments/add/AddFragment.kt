package com.example.travelapp.fragments.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelapp.R
import com.example.travelapp.data.User
import com.example.travelapp.data.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var userViewModel: UserViewModel
    private val args by navArgs<AddFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.tripInfo !=null){

            departure.text = args.tripInfo?.departure?.toEditable()
            date.text = args.tripInfo?.departureDate?.toEditable()
            time.text = args.tripInfo?.departureTime?.toEditable()
            destination.text = args.tripInfo?.destination?.toEditable()
            date2.text = args.tripInfo?.arrivalDate?.toEditable()
            time2.text = args.tripInfo?.arrivalTime?.toEditable()
            trip_type_textview.text = args.tripInfo?.tripType ?: "Trip Type"

            add_trip.text = ("UPDATE TRIP")

            
        }

        // Inflate the layout for this fragment
       // val view = inflater.inflate(R.layout.fragment_add, container, false)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        add_trip.setOnClickListener {
            val button = it as Button
            if(button.text.toString()=="UPDATE TRIP"){
                insertDataToDatabase()
            }else{
                insertDataToDatabase()
            }

        }
        departure.setOnFocusChangeListener { v, hasFocus ->
           setTitleCase(v, hasFocus)
        }
        destination.setOnFocusChangeListener { v, hasFocus ->
            setTitleCase(v, hasFocus)
        }
        date.setOnClickListener {
            selectDate(it)
        }
        date2.setOnClickListener {
            selectDate(it)
        }
        time.setOnClickListener {
            selectTime(it)
        }
        time2.setOnClickListener {
            selectTime(it)
        }

            view.add_trip_menu.setOnClickListener {

            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.inflate(R.menu.trip_type_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->

                when (menuItem.itemId) {
                    R.id.business -> {
                        trip_type_textview.text = menuItem.title
                    }
                    R.id.health -> {
                        trip_type_textview.text = menuItem.title
                    }
                    R.id.education -> {
                        trip_type_textview.text = menuItem.title
                    }
                    R.id.vacation -> {
                        trip_type_textview.text = menuItem.title

                    }
                }
                true
            }
            popupMenu.show()
        }

       // return view
    }

    private fun insertDataToDatabase() {


        val addDeparture = departure.text.toString()
        val departDate = date.text.toString()
        val departTime = time.text.toString()
        val destination = destination.text.toString()
        val arrivalDate = date2.text.toString()
        val arrivalTime = time2.text.toString()
        val tripType = trip_type_textview.text.toString()
        if (!(addDeparture.isEmpty()|| departDate.isEmpty()|| departTime.isEmpty()|| destination.isEmpty()|| arrivalDate.isEmpty()
            ||arrivalTime.isEmpty()|| tripType.isEmpty())) {

            if (args.tripInfo != null) {
                val user = User(
                    args.tripInfo!!.id,
                    addDeparture,
                    departDate,
                    departTime,
                    destination,
                    arrivalDate,
                    arrivalTime,
                    tripType
                )
                userViewModel.updateUser(user)
                Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            } else {

                //create user object
                val user = User(
                    0,
                    addDeparture,
                    departDate,
                    departTime,
                    destination,
                    arrivalDate,
                    arrivalTime,
                    tripType
                )
                //add data to database
                userViewModel.addUser(user)
                Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }
        }else{
            Toast.makeText(requireContext(),"Not Successful",Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectDate(v: View) {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, mYear, mMonth, dayOfMonth ->
                val formattedDate = formatDate(mYear, mMonth, dayOfMonth)

                (v as EditText).text = formattedDate.toEditable()
            },
            year,
            month,
            day
        ).show()
    }

    private fun selectTime(v: View) {
        val c = Calendar.getInstance()

        val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
        val minOfDay = c.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hour, min ->
                val am_pm = if (hour >= 12) "PM" else "AM"
                val time =
                    if (hour == 12 || hour == 0) "12 : $min $am_pm" else "${hour % 12} : $min $am_pm"
                (v as EditText).text = time.toEditable()
            },
            hourOfDay,
            minOfDay,
            false
        ).show()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun formatDate(year: Int, month: Int, day: Int): String {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.of(year, month, day)
            val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
            val answer: String = date.format(formatter)
            Log.d("answer", answer)
            answer
        } else {

            var date = Date(year - 1900, month, day)
            val formatter = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
            val answer: String = formatter.format(date)
            Log.d("answer", answer)
            Log.d("answerYear", year.toString())
            answer

        }
    }

    fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") { it.toLowerCase().capitalize() }


    private fun setTitleCase(v: View, hasFocus: Boolean) {
        val editText = v as EditText
        if (!hasFocus) {
            editText.text =  editText.text.toString().capitalizeWords().toEditable()
        }
    }
}

