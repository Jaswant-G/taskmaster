package mad.lab.taskmaster.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*
import mad.lab.taskmaster.R
import mad.lab.taskmaster.firebase.FirestoreClass
import mad.lab.taskmaster.models.User


// START
class SignUpActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_up)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //  Call the setup actionBar function.
        setupActionBar()
    }

    // A function for setting up the actionBar
    /**
     * A function for actionBar Setup.
     */
    fun userRegisteredSuccess(){
        Toast.makeText(
            this,
             " you have successfully registered ",
            Toast.LENGTH_LONG
        ).show()

        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }


    private fun setupActionBar() {
//        val toolbar = findViewById<View>(R.id.toolbar_sign_up_activity) as Toolbar


        // using toolbar as ActionBar
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }

        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser()
    {
        val name:String = et_name.text.toString().trim{ it <= ' '}
        val email:String = et_email.text.toString().trim{ it <= ' '}
        val password:String = et_password.text.toString().trim{ it <= ' '}

        if(validateForm(name, email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        FirestoreClass().registerUser(this, user)
                    } else {
                        Toast.makeText(
                            this,
                            "Registration Failed!", Toast.LENGTH_SHORT).show()

                    }
                }
        }


    }


    private fun validateForm(name: String, email:String, password: String): Boolean {

        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter your name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter your email address")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Password must not be empty!")
                false
            }
            else->{
                true
            }
        }

    }
}