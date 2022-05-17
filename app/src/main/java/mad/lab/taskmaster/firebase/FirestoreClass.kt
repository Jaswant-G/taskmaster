package mad.lab.taskmaster.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import mad.lab.taskmaster.activities.MainActivity
import mad.lab.taskmaster.activities.SignInActivity
import mad.lab.taskmaster.activities.SignUpActivity
import mad.lab.taskmaster.utils.Constants
import kotlin.math.log

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()


    fun registerUser(activity: SignUpActivity, userInfo: mad.lab.taskmaster.models.User)
    {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener{
                e->
                Log.e(activity.javaClass.simpleName, "Error writing document", e)
            }
    }

    fun signInUser(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(mad.lab.taskmaster.models.User::class.java)
                when(activity){
                    is SignInActivity ->{
                        if(loggedInUser != null)
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        if(loggedInUser != null)
                        activity.updateNavigationUserDetails(loggedInUser)
                    }
                }

            }.addOnFailureListener{
                    e->
                when(activity){
                    is SignInActivity ->{
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }


                Log.e("SignInUser", "Error writing document", e)
            }
    }

    fun getCurrentUserId(): String{

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}