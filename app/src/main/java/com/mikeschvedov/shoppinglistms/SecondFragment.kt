package com.mikeschvedov.shoppinglistms

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.mikeschvedov.shoppinglistms.data.network.RetrofitInstance
import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification
import com.mikeschvedov.shoppinglistms.databinding.FragmentSecondBinding
import com.mikeschvedov.shoppinglistms.util.prepareNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val firebaseMessaging = FirebaseMessaging.getInstance()

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/

        //TODO : option to send to a specific user/device
        //Getting the unique token for this device
      /*  firebaseMessaging.token.addOnSuccessListener {
            binding.textview.text = it
        }*/

        //Subscribe to a topic
        firebaseMessaging.subscribeToTopic(TOPIC)

        // Trigger the notification sending
       /* binding.sendNotification.setOnClickListener {
            val notificationTitle = "get title some way"
            val notificationMessage = "get message some way"

            prepareNotification(notificationTitle, notificationMessage, TOPIC).also {
                sendNotification(it)
            }
        }*/

        binding.mylistToolbar.inflateMenu(R.menu.menu_main)

        binding.mylistToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete_selected -> {
                   // heartsViewModel.resetCounter()
                    true
                }
                R.id.delete_all -> {
                  //  heartsViewModel.resetCounter()
                    true
                }
                else -> false
            }
        }

        return root
    }


    //TODO: MOVE TO CONTENT_MEDIATOR CLASS
    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("sendNotification", "Response ${response.body().toString()}")
                } else {
                    Log.e("sendNotification", response.errorBody().toString())
                }
            } catch (e: Exception) {
                //TODO: create logger class
                Log.e("sendNotification", e.toString())
            }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

