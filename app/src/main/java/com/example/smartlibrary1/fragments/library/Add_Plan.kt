package com.example.smartlibrary1.fragments.library



import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smartlibrary1.data.Plan
import com.example.smartlibrary1.databinding.ActivityAddPlan2Binding
import com.example.smartlibrary1.helper.BookDatabaseHelper
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.util.Calendar

@AndroidEntryPoint

class Add_Plan : AppCompatActivity() {
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var binding: ActivityAddPlan2Binding
    private lateinit var db:BookDatabaseHelper
    private var idbook: Int? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPlan2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        db = BookDatabaseHelper(this)

        idbook = intent.getIntExtra("idPlanbook", -1)

        if(idbook == -1){
            Toast.makeText(this, "Error retrieving book ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val existingPlan = db.getPlanByBookID(idbook!!)
        if(existingPlan != null){
            binding.titleET.setText(existingPlan.title)
            binding.messageET.setText(existingPlan.message)
            val day = existingPlan.day
            val month = existingPlan.month - 1
            val year = existingPlan.year
            val hour = existingPlan.hour
            val minute = existingPlan.minute
            binding.datePicker.updateDate(year, month, day)
            binding.timePicker.hour = hour
            binding.timePicker.minute = minute
        }




        createNotificationChannel()
        binding.submitBtn.setOnClickListener {

            if(existingPlan != null){
                canccelNotificationNoAlert()
                db.deletePlanByBookId(idbook!!)

            }
            scheduleNotification()
            val title = binding.titleET.text.toString()
            val message = binding.messageET.text.toString()
            val day = binding.datePicker.dayOfMonth
            val month = binding.datePicker.month + 1
            val year = binding.datePicker.year
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            val plan = Plan(idbook!!, title, message, day, month, year, hour, minute)
            db.insertPlan(plan)
        }

        binding.deletePlanBtn.setOnClickListener {
            val success = db.deletePlanByBookId(idbook!!)
            if(success){
                binding.titleET.text?.clear()
                binding.messageET.text?.clear()
            }


            canccelNotification()

        }



//        val backBtn = findViewById<ImageButton>(R.id.backBtn)
//        backBtn.setOnClickListener {
//            onBackPressed()
//        }





    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This app needs the Notification permission to remind you about your plans.")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission was granted, proceed with the notification
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission was denied, show a message to the user
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun canccelNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val uniqueID = idbook!!
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            uniqueID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(uniqueID)

        AlertDialog.Builder(this)
            .setTitle("Notification Canceled")
            .setMessage("The scheduled notification has been canceled.")
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun canccelNotificationNoAlert() {
        val intent = Intent(applicationContext, Notification::class.java)
        val uniqueID = idbook!!
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            uniqueID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(uniqueID)

    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = binding.titleET.text.toString()
        val message = binding.messageET.text.toString()
        val uniqueID = idbook!!
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        intent.putExtra("uniqueID",uniqueID)



        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            uniqueID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis

    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + ", " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}