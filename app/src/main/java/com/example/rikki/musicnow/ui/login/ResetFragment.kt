package com.example.rikki.musicnow.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rikki.musicnow.LoginActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentResetBinding
import com.example.rikki.musicnow.utils.Constants
import com.example.rikki.musicnow.utils.Constants.mismatch
import com.example.rikki.musicnow.utils.Constants.success
import com.example.rikki.musicnow.utils.SPController
import java.util.*

class ResetFragment : Fragment() {

    private var binding: FragmentResetBinding? = null
    private val model: LoginActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetBinding.inflate(inflater, container, false)

        // reset password
        binding?.resetBtn?.setOnClickListener {
            if (LoginActivity.isInternetAvailable(requireActivity())) {
                if (checkInputs()) {
                    model.resetPassword(
                            binding?.phone?.text.toString(),
                            binding?.oldPswd?.text.toString(),
                            binding?.newPswd?.text.toString()
                    )
                }
            }
        }

        // observe reset response
        model.getResetResponse().observe(viewLifecycleOwner, { response ->
            when {
                response.contains(success, true) -> {
                    Toast.makeText(requireActivity(), response.capitalize(Locale.getDefault()), Toast.LENGTH_SHORT).show()
                    val mobile = binding?.phone?.text.toString()
                    val password = binding?.newPswd?.text.toString()
                    if (mobile != SPController.getInstance(requireActivity()).getUserMobile()) {
                        SPController.getInstance(requireActivity()).apply {
                            deleteUser()
                            saveUser("", mobile, "", password)
                        }
                    } else {
                        SPController.getInstance(requireActivity()).apply {
                            updatePassword(password)
                        }
                    }
                    findNavController().navigate(R.id.action_reset_to_login)
                }
                response.contains(mismatch, true) -> {
                    LoginActivity.showAlertDialog(getString(R.string.password_error_msg))
                }
                else -> {
                    LoginActivity.showAlertDialog("Error: $response")
                }
            }
        })

        return binding?.root
    }

    private fun checkInputs(): Boolean {
        var result = true
        if (binding?.phone?.text.isNullOrBlank()) {
            result = false
            binding?.phone?.error = getString(R.string.hint_phone_err)
        }
        val oldPswd = binding?.oldPswd?.text
        if (oldPswd == null) {
            result = false
            binding?.oldPswd?.error = getString(R.string.hint_password_err)
        } else if (!Constants.passwordPattern.matcher(oldPswd).matches()){
            result = false
            binding?.oldPswd?.error = getString(R.string.password_hint)
        }
        val newPswd = binding?.newPswd?.text
        if (newPswd == null) {
            result = false
            binding?.newPswd?.error = getString(R.string.hint_password_err)
        } else if (!Constants.passwordPattern.matcher(newPswd).matches()){
            result = false
            binding?.newPswd?.error = getString(R.string.password_hint)
        }
        val confirmPswd = binding?.confirmPswd?.text
        if (confirmPswd == null) {
            result = false
            binding?.confirmPswd?.error = getString(R.string.hint_password_err)
        } else if (!Constants.passwordPattern.matcher(confirmPswd).matches()){
            result = false
            binding?.confirmPswd?.error = getString(R.string.password_hint)
        } else if (newPswd != null && newPswd != confirmPswd) {
            result = false
            binding?.confirmPswd?.error = getString(R.string.hint_confirm_password)
        }
        return result
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
