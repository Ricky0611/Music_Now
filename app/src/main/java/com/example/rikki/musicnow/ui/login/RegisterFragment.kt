package com.example.rikki.musicnow.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rikki.musicnow.LoginActivity
import com.example.rikki.musicnow.R
import com.example.rikki.musicnow.databinding.FragmentRegisterBinding
import com.example.rikki.musicnow.utils.Constants
import com.example.rikki.musicnow.utils.Constants.exist
import com.example.rikki.musicnow.utils.Constants.success
import com.example.rikki.musicnow.utils.SPController
import java.util.*

class RegisterFragment : Fragment() {

    private var binding : FragmentRegisterBinding? = null
    private val model: LoginActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // register
        binding?.regBtn?.setOnClickListener {
            if (LoginActivity.isInternetAvailable(requireActivity())) {
                if (checkInputs()) {
                    model.register(
                            binding?.name?.text.toString(),
                            binding?.email?.text.toString(),
                            binding?.phone?.text.toString(),
                            binding?.password?.text.toString()
                    )
                }
            }
        }

        // observe register response
        model.getRegisterResponse().observe(viewLifecycleOwner, { response ->
            when {
                response.startsWith(success, true) -> {
                    Toast.makeText(requireActivity(), response.capitalize(Locale.getDefault()), Toast.LENGTH_SHORT).show()
                    SPController.getInstance(requireActivity()).apply {
                        deleteUser()
                        saveUser(
                                binding?.name?.text.toString(),
                                binding?.phone?.text.toString(),
                                binding?.email?.text.toString(),
                                binding?.password?.text.toString()
                        )
                    }
                    findNavController().navigate(R.id.action_register_to_login)
                }
                response.contains(exist, true) -> {
                    LoginActivity.showAlertDialog(getString(R.string.reg_error_msg))
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
        if (binding?.name?.text.isNullOrBlank()) {
            result = false
            binding?.name?.error = getString(R.string.hint_name_err)
        }
        val email = binding?.email?.text
        if (email == null) {
            result = false
            binding?.email?.error = getString(R.string.hint_email_err)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = false
            binding?.email?.error = getString(R.string.email_hint)
        }
        if (binding?.phone?.text.isNullOrBlank()) {
            result = false
            binding?.phone?.error = getString(R.string.hint_phone_err)
        }
        val password = binding?.password?.text
        if (password == null) {
            result = false
            binding?.password?.error = getString(R.string.hint_password_err)
        } else if (!Constants.passwordPattern.matcher(password).matches()){
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
