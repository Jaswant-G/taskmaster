package mad.lab.taskmaster.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.app_bar_main.*
import mad.lab.taskmaster.R
import mad.lab.taskmaster.firebase.FirestoreClass
import mad.lab.taskmaster.models.User
import mad.lab.taskmaster.utils.Constants
import java.io.IOException

//class MyProfileActivity : BaseActivity() {
//    companion object {
//        private const val READ_STORAGE_PERMISSION_CODE = 1
//        private const val PICK_IMAGE_REQUEST_CODE = 2
//    }
//
//    private var mSelectedImageFileURI: Uri? = null
//    private var mProfileImageURL : String = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_profile)
//        setupActionBar()
//
//        FirestoreClass().loadUserData(this)
//
//        iv_profile_user_image.setOnClickListener{
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                showImageChooser()
//            }else{
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    READ_STORAGE_PERMISSION_CODE
//                )
//            }
//
//        }
//        btn_update.setOnClickListener {
//            if(mSelectedImageFileURI != null)
//            {
//                uploadUserImage()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode == READ_STORAGE_PERMISSION_CODE){
//            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                showImageChooser()
//            }
//        }else{
//            Toast.makeText(
//                this,
//                "Oops, you just denied the permission for storage",
//                Toast.LENGTH_LONG
//            ) .show()
//        }
//    }
//
//
//    private fun showImageChooser(){
//        var galleryIntent = Intent(Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//
//        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null){
//            mSelectedImageFileURI = data.data
//            try{
//                Glide
//                    .with(this@MyProfileActivity)
//                    .load(mSelectedImageFileURI)
//                    .centerCrop()
//                    .placeholder(R.drawable.ic_user_place_holder)
//                    .into(iv_profile_user_image)
//
//            }catch (e: IOException){
//                e.printStackTrace()
//            }
//
//        }
//    }
//
//    private fun setupActionBar(){
//        setSupportActionBar(toolbar_my_profile_activity)
//        val actionBar = supportActionBar
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
//            actionBar.title = resources.getString(R.string.my_profile_title)
//        }
//
//        toolbar_my_profile_activity.setNavigationOnClickListener{
//            onBackPressed()
//        }
//    }
//
//    fun setUserDataInUI(user: User){
//        Glide
//            .with(this@MyProfileActivity)
//            .load(user.image)
//            .centerCrop()
//            .placeholder(R.drawable.ic_user_place_holder)
//            .into(iv_profile_user_image)
//
//        et_name.setText(user.name)
//        et_email.setText(user.email)
//        if(user.mobile != 0L){
//            et_mobile.setText(user.mobile.toString())
//        }
//    }
//
//    private fun uploadUserImage(){
//        showProgressDialog(resources.getString(R.string.please_wait))
//
//        if(mSelectedImageFileURI != null){
//            val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
//                "USER_IMAGE" + System.currentTimeMillis()
//                        + "." + getFileExtension(mSelectedImageFileURI))
//
//            sRef.putFile(mSelectedImageFileURI!!).addOnSuccessListener {
//                taskSnapshot ->
//                Log.i(
//                    "Firebase Image URL",
//                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
//                )
//
//                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                    uri ->
//                    Log.i("Downloadable Image URL", uri.toString())
//
//                    mProfileImageURL = uri.toString()
//
//
//                }
//            }.addOnFailureListener{
//                exception ->
//                Toast.makeText(
//                    this@MyProfileActivity,
//                    exception.message,
//                    Toast.LENGTH_LONG
//                ).show()
//
//                hideProgressDialog()
//            }
//        }
//    }
//
//    private fun getFileExtension(uri: Uri?): String?{
//        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
//    }
//}

class MyProfileActivity : BaseActivity() {

    // Add a global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    // TODO (Step 6: Add the global variables for UserDetails and Profile Image URL.)
    // START
    // A global variable for user details.
    private lateinit var mUserDetails: User

    // A global variable for a user profile image URL
    private var mProfileImageURL: String = ""
    // END

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setupActionBar()

        FirestoreClass().loadUserData(this@MyProfileActivity)

        iv_profile_user_image.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        // TODO (Step 10: Add a click event for updating the user profile data to the database.)
        // START
        btn_update.setOnClickListener {

            // Here if the image is not selected then update the other details of user.
            if (mSelectedImageFileUri != null) {

                uploadUserImage()
            } else {

                showProgressDialog(resources.getString(R.string.please_wait))

                // Call a function to update user details in the database.
                updateUserProfileData()
            }
        }
        // END
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                // Load the user image in the ImageView.
                Glide
                    .with(this@MyProfileActivity)
                    .load(Uri.parse(mSelectedImageFileUri.toString())) // URI of the image
                    .centerCrop() // Scale type of the image.
                    .placeholder(R.drawable.ic_user_place_holder) // A default place holder
                    .into(iv_profile_user_image) // the view in which the image will be loaded.
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.my_profile_title)
        }

        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to set the existing details in UI.
     */
    fun setUserDataInUI(user: User) {

        // TODO (Step 7: Initialize the user details variable)
        // START
        // Initialize the user details variable
        mUserDetails = user
        // END

        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_profile_user_image)

        et_name.setText(user.name)
        et_email.setText(user.email)
        if (user.mobile != 0L) {
            et_mobile.setText(user.mobile.toString())
        }
    }

    private fun uploadUserImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null) {

            //getting the storage reference
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + Constants.getFileExtension(
                    this,
                    mSelectedImageFileUri
                )
            )

            //adding the file to reference
            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // The image upload is success
                    Log.e(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )

                    // Get the downloadable url from the task snapshot
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())

                            // assign the image url to the variable.
                            mProfileImageURL = uri.toString()

                            // Call a function to update user details in the database.
                            updateUserProfileData()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this@MyProfileActivity,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()

                    hideProgressDialog()
                }
        }
    }


    // END

    // TODO (Step 9: Update the user profile details into the database.)
    // START
    /**
     * A function to update the user profile details into the database.
     */
    private fun updateUserProfileData() {

        val userHashMap = HashMap<String, Any>()

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
        }

        if (et_name.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = et_name.text.toString()
        }

        if (et_mobile.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = et_mobile.text.toString().toLong()
        }

        // Update the data in the database.
        FirestoreClass().updateUserProfileData(this@MyProfileActivity, userHashMap)
    }
    // END

    // TODO (Step 4: Create a function to notify the user profile is updated successfully.)
    // START
    /**
     * A function to notify the user profile is updated successfully.
     */
    fun profileUpdateSuccess() {

        hideProgressDialog()

        setResult(Activity.RESULT_OK)
        finish()
    }


}