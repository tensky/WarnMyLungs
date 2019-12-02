package id.tensky.warnmylungs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.tensky.warnmylungs.callbacks.CallbackAPI
import kotlinx.android.synthetic.main.activity_otp.*
import org.json.JSONObject

class OtpActivity : AppCompatActivity() {
    lateinit var phoneNumber : String
    lateinit var loginSharedPreferences: SharedPreferences
    lateinit var loginEditor :SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        supportActionBar?.hide()
        loginSharedPreferences = getSharedPreferences("WMLsLogin", Context.MODE_PRIVATE)
        loginEditor = loginSharedPreferences.edit()

        phoneNumber = intent.getStringExtra("phoneNumber")!!
        TembakAPI.getOtpCode(callback, phoneNumber)
        //Otp Edittext tricks
        otp_firstNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (otp_firstNumber.text.toString().length == 1) {
                    otp_secondNumber.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        otp_secondNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (otp_secondNumber.text.toString().length == 1) {
                    otp_thirdNumber.requestFocus()
                }else if(otp_secondNumber.text.toString().isEmpty()){
                    otp_firstNumber.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        otp_thirdNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (otp_thirdNumber.text.toString().length == 1) {
                    otp_fourthNumber.requestFocus()
                }else if(otp_thirdNumber.text.toString().isEmpty()){
                    otp_secondNumber.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        otp_fourthNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (otp_fourthNumber.text.toString().isEmpty()) {
                    otp_thirdNumber.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    val callback = object : CallbackAPI {
        override fun onCallback(response: JSONObject) {
            otp_confirmButton.setOnClickListener{
                val otpCode = otp_firstNumber.text.toString() + otp_secondNumber.text.toString() + otp_thirdNumber.text.toString() + otp_fourthNumber.text.toString()
                if(response.getString("verify") == otpCode){
                    loginEditor.putBoolean("login", true)
                    loginEditor.commit()
                    val mainIntent = Intent(this@OtpActivity, IntroActivity::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(mainIntent)
                    finish()
                }else{
                    Toast.makeText(this@OtpActivity, "Wrong OTP code!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
