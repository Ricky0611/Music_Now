package com.example.rikki.musicnow.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rikki.musicnow.HomeActivity
import com.example.rikki.musicnow.LoginActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentLoginBinding
import com.example.rikki.musicnow.utils.Constants.fail
import com.example.rikki.musicnow.utils.Constants.passwordPattern
import com.example.rikki.musicnow.utils.Constants.success
import com.example.rikki.musicnow.utils.SPController
import java.util.*

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val model: LoginActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // login
        binding?.signInBtn?.setOnClickListener {
            if (LoginActivity.isInternetAvailable(requireActivity())) {
                if (checkInputs()) {
                    model.login(binding?.mobile?.text.toString(), binding?.password?.text.toString())
                }
            }
        }

        // register
        binding?.registerBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        // reset password
        binding?.resetBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_reset)
        }

        // observe login response
        model.getLoginResponse().observe(viewLifecycleOwner, { response ->
            val result = response.getString(0)
            when (result) {
                success -> {
                    Toast.makeText(requireActivity(), result.capitalize(Locale.getDefault()), Toast.LENGTH_SHORT).show()
                    val mobile = response.getString(1)
                    if (!mobile.equals(SPController.getInstance(requireActivity()).getUserMobile())) {
                        val password = binding?.password?.text.toString()
                        SPController.getInstance(requireActivity()).apply {
                            deleteUser()
                            saveUser("Ricky", mobile, "ricky@gmail.com", password)
                        }
                    }
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                fail -> {
                    LoginActivity.showAlertDialog(getString(R.string.signIn_error_msg))
                }
                else -> { // error
                    LoginActivity.showAlertDialog("Error: $result")
                }
            }
        })

        // display saved user data if any
        if (SPController.getInstance(requireActivity()).hasUserLoggedIn()) {
            binding?.mobile?.setText(SPController.getInstance(requireActivity()).getUserMobile())
            binding?.password?.setText(SPController.getInstance(requireActivity()).getUserPassword())
        }

        return binding?.root
    }

    private fun checkInputs(): Boolean {
        var result = true
        if (binding?.mobile?.text.isNullOrBlank()) {
            result = false
            binding?.mobile?.error = getString(R.string.hint_phone_err)
        }
        val password = binding?.password?.text
        if (password == null) {
            result = false
            binding?.password?.error = getString(R.string.hint_password_err)
        } else if (!passwordPattern.matcher(password).matches()){
            result = false
            binding?.password?.error = getString(R.string.password_hint)
        }
        return result
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
