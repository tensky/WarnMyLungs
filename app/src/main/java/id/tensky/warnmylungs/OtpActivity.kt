package id.tensky.warnmylungs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_otp.*

class OtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        supportActionBar?.hide()

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
}
