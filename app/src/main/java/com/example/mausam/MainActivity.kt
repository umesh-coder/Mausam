package com.example.mausam


import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class MainActivity : AppCompatActivity() {

    private val locationStateReceiver = LocationStateReceiver()

    private val locationPermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intentFilter = IntentFilter("android.location.PROVIDERS_CHANGED")
        registerReceiver(locationStateReceiver, intentFilter)

        if (!isLocationEnabled()) {
            Toast.makeText(
                this,
                "Your location provider is turned off. Please turn it on.",
                Toast.LENGTH_SHORT
            ).show()
            requestLocationPermissions()
        } else {
            Toast.makeText(
                this,
                "Your location provider is already ON.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        // Unregister the receiver
        unregisterReceiver(locationStateReceiver)
    }

    private fun requestLocationPermissions() {
        Dexter.withActivity(this).withPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                // Check if all permissions are granted
                if (report!!.areAllPermissionsGranted()) {
                    Toast.makeText(
                        this@MainActivity,
                        "All the permissions are granted..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // User denied permissions, show a message or exit the app
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest?>?, token: PermissionToken?
            ) {
                // Show a dialog explaining why the app needs location permissions
                showSettingsDialog()
            }
        }).onSameThread().check()


    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint
    private fun requestLocationData(){




    }

    // below is the shoe setting dialog method which is use to display a dialogue message.
    private fun showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        val builder = AlertDialog.Builder(this@MainActivity)


        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions")


        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog: DialogInterface, which: Int ->
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel()

            // below is the intent from which we are redirecting our user.
            val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.setData(uri)
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int ->
            // this method is called when user click on negative button.
            dialog.cancel()
        }

        // below line is used to display our dialog
        builder.show()
    }


    /**
     * A function which is used to verify that the location or GPS is enabled or not.
     */
    private fun isLocationEnabled(): Boolean {

        // This provides access to the system location services.
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager




        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}

