package com.burcaliahmadov.groupchat.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.burcaliahmadov.groupchat.MainActivity
import com.burcaliahmadov.groupchat.databinding.ActivityProfileBinding
import com.burcaliahmadov.groupchat.model.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage:FirebaseStorage
    private lateinit var database:FirebaseDatabase
    private lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher:ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerLauncher()

        dialog=AlertDialog.Builder(this)
            .setMessage("Updating Profile...")
            .setCancelable(false)

        database=FirebaseDatabase.getInstance()
        auth=FirebaseAuth.getInstance()
        storage=FirebaseStorage.getInstance()

        binding.userImage.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(it,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission",
                        View.OnClickListener {
                            //request permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                }
                else{
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }else{
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //startactivity
                activityResultLauncher.launch(intentToGallery)
            }

        }
        binding.continueBtn.setOnClickListener {
            if(binding.username.text!!.isEmpty()){
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show()
            }else if(selectedImg==null) {
                Toast.makeText(this, "Please enter your Image", Toast.LENGTH_SHORT).show()
            }else uploadData()


        }

    }
    private fun uploadData(){
        val uuid=UUID.randomUUID()
        val imagname="$uuid.jpg"
        val reference =storage.reference.child("Profile").child(imagname)
        reference.putFile(selectedImg).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener {uri->
                uploadInfo(uri.toString())
            }
            }.addOnFailureListener {
            Toast.makeText(this@ProfileActivity, "${it.localizedMessage}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun uploadInfo(imageUrl: String) {
        val user =UserModel(auth.uid.toString(),binding.username.text.toString(),
            auth.currentUser?.phoneNumber.toString()!!,imageUrl)
            database.reference.child("user").child(auth.uid.toString()).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ProfileActivity,MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
    }



    fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result!=null){
                val intentFromResult=result.data
                if (intentFromResult!=null){
                    selectedImg=intentFromResult.data!!
                    if(selectedImg!=null){
                        binding.userImage.setImageURI(selectedImg)
                    }
                }
            }
        }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if(result){
                //intentToGallery
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }else{
                //permission denied
                Toast.makeText(this@ProfileActivity, "Permission needed!", Toast.LENGTH_LONG).show()
            }


        }
    }
}