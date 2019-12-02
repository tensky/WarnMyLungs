package id.tensky.warnmylungs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.tensky.warnmylungs.callbacks.CallbackAPI
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
        signin_signUpButton.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        
        val callbackAPI = object : CallbackAPI {
            override fun onCallback(response: JSONObject) {
                val otpIntent = Intent(this@SignInActivity, OtpActivity::class.java)
                otpIntent.putExtra("phoneNumber", signin_phone.text.toString())
                startActivity(otpIntent)
            }
        }

        signin_signInButton.setOnClickListener{
            if(signin_phone.text.isEmpty()){
                Toast.makeText(this, "Silahkan isi dengan benar!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            TembakAPI.postLogin(callbackAPI, signin_phone.text.toString())

        }
    }
}
