package id.tensky.warnmylungs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.tensky.warnmylungs.callbacks.CallbackAPI
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        signup_signUpButton.setOnClickListener{
            if(signup_email.text.isEmpty() || signup_nama.text.isEmpty() || signup_nik.text.isEmpty() || signup_phone.text.isEmpty()){
                Toast.makeText(this, "Silahkan isi dengan benar!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            TembakAPI.postRegisterUser(callback, signup_nik.text.toString(), signup_phone.text.toString(), signup_nama.text.toString(), signup_email.text.toString())
        }
    }

    val callback = object : CallbackAPI {
        override fun onCallback(response: JSONObject) {
            val otpIntent = Intent(this@SignUpActivity, OtpActivity::class.java)
            otpIntent.putExtra("phoneNumber", signup_phone.text.toString())
            startActivity(otpIntent)
        }
    }
}
