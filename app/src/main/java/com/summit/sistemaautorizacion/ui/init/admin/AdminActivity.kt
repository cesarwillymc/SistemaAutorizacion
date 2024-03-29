package com.summit.sistemaautorizacion.ui.init.admin

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseActivity
import com.summit.sistemaautorizacion.ui.camerax.ViewModelMain
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_transparent.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class AdminActivity : BaseActivity(),KodeinAware {
    override val kodein: Kodein by kodein()
    private lateinit var cameraVM: ViewModelMain


    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraVM= run{
            ViewModelProvider(this).get(ViewModelMain::class.java)
        }
        setSupportActionBar(toolbar_transparent)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_personal,R.id.nav_profile,R.id.nav_comerciante,R.id.nav_scanner))
        //, R.id.register,
        //            R.id.registerAdmin, R.id.sendSms,R.id.validateNumber
        navController= findNavController(R.id.am_fragment)
        setupActionBarWithNavController(navController,appBarConfiguration)
        nav_view.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Dexter.withContext(this).withPermissions(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    when(destination.id){
                        R.id.nav_scanner->{
                            toolbar_main.hide()
                            nav_view.show()
                            cameraVM.imageSelect.postValue("")
                        }
                        R.id.nav_comerciante->{
                            toolbar_main.hide()
                            nav_view.show()
                            cameraVM.imageSelect.postValue("")
                        }
                        R.id.nav_personal->{
                            toolbar_main.hide()
                            nav_view.show()
                            cameraVM.imageSelect.postValue("")
                        }
                        R.id.nav_profile->{
                            toolbar_main.hide()
                            nav_view.show()
                            cameraVM.imageSelect.postValue("")
                        }
                        R.id.galleryFragment->{
                            toolbar_main.hide()
                            nav_view.hide()
                        }
                        R.id.registerPersonalFragment->{
                            toolbar_main.show()
                            nav_view.hide()
                        }
                        R.id.updatePersonalFragment->{
                            toolbar_main.show()
                            nav_view.hide()
                        }
                        else->{
                            cameraVM.imageSelect.postValue("")
                            toolbar_main.show()
                            nav_view.hide()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }
            }).check()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
    override fun getLayout()= R.layout.activity_main
}