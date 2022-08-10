package mad.lab.taskmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_intro.*
import mad.lab.taskmaster.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        btn_sign_up_intro.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btn_sign_in_intro.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }
}