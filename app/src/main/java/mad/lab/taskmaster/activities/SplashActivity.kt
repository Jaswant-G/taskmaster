package mad.lab.taskmaster.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import mad.lab.taskmaster.R
import mad.lab.taskmaster.firebase.FirestoreClass

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface = Typeface.createFromAsset(assets, "Ubuntu-L.ttf")

        val tvAppName: TextView = findViewById(R.id.tv_app_name)
        tvAppName.typeface = typeFace

        Handler().postDelayed({

            var currentUserID = FirestoreClass().getCurrentUserId()

            if(currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                startActivity(Intent(this, IntroActivity::class.java))
            }

            finish()
        }, 2500)
    }
}