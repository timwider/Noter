package com.example.noter.presentation.dialogs.create_notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noter.databinding.CreateFolderFragmentBinding
import com.example.noter.databinding.CreateNotificationFragmentBinding
import com.example.noter.presentation.services.NotificationService
import com.example.noter.utils.Calendar
import com.example.noter.utils.NOTIFICATION_INTENT_TITLE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

const val NOTIFICATION_TITLE_FROM_NOTES = "notification_title_from_note"
const val ONE_YEAR_IN_MILLIS = 31556926000

class CreateNotificationFragment: BottomSheetDialogFragment() {

    private lateinit var binding: CreateNotificationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateNotificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val calendar = java.util.Calendar.getInstance()
        binding.notificationDatePicker.minDate = calendar.timeInMillis
        binding.notificationDatePicker.maxDate = calendar.timeInMillis + ONE_YEAR_IN_MILLIS

        binding.btnCancel.setOnClickListener { dialog?.dismiss() }
        binding.btnConfirm.setOnClickListener { confirmTimeListener() }


        super.onViewCreated(view, savedInstanceState)
    }

    private fun confirmTimeListener() {
    }

    private fun scheduleNotification(dateInMillis: Long) {
        val title = arguments?.getString(NOTIFICATION_TITLE_FROM_NOTES)
        val intent = Intent(requireContext(), NotificationService::class.java)
        intent.putExtra(NOTIFICATION_INTENT_TITLE, title)
        val pendingIntent = PendingIntent.getService(requireContext(), 7, intent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            dateInMillis,
            pendingIntent
        )
    }

    private fun getDateInMillis() {

    }
}