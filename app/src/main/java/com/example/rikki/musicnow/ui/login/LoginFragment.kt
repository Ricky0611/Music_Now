package com.example.rikki.musicnow.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
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
import com.facebook.*
import com.facebook.login.LoginResult
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

        // facebook login
        callbackManager = CallbackManager.Factory.create()
        binding?.facebookBtn?.apply {
            setPermissions(listOf(PUBLIC_PROFILE, EMAIL))
            fragment = this@LoginFragment
            registerCallback(
                callbackManager,
                object: FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        Log.d("Facebook", "onSuccess - ${result.toString()}")
                        getUserProfile(result?.accessToken, result?.accessToken?.userId)
                    }

                    override fun onCancel() {
                        Log.d("Facebook", "onCancel")
                    }

                    override fun onError(error: FacebookException?) {
                        Log.d("Facebook", "onError - ${error.toString()}")
                        LoginActivity.showAlertDialog(error?.message?.let {
                            "Error: $it"
                        } ?: run {
                            getString(R.string.fb_login_failed)
                        })
                    }
                }
            )
        }

        // observe login response
        model.getLoginResponse().observe(viewLifecycleOwner, { response ->
            val result = response.getString(0)
            when (result) {
                success -> {
                    Toast.makeText(
                        requireActivity(),
                        result.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        },
                        Toast.LENGTH_SHORT
                    ).show()
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
            if (SPController.getInstance(requireActivity()).getUserPassword().isNotEmpty()) {
                binding?.mobile?.setText(
                    SPController.getInstance(requireActivity()).getUserMobile()
                )
                binding?.password?.setText(
                    SPController.getInstance(requireActivity()).getUserPassword()
                )
            }
        }

        return binding?.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("LoginFragment", "onActivityResult - $requestCode, $resultCode, ${data?.extras.toString()}")
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun getUserProfile(token: AccessToken?, userId: String?) {
        val parameters = bundleOf(FIELDS to FIELD_LIST)
        val request = GraphRequest(token, "/$userId/", parameters, HttpMethod.GET) {
            val response = it.jsonObject
            val mobile = if (response.has(ID)) {
                response.getString(ID)
            } else {
                ""
            }
            Log.d("Facebook", "getUserProfile - ID: $mobile")
            val name = if (response.has(NAME)) {
                response.getString(NAME)
            } else {
                "Ricky"
            }
            Log.d("Facebook", "getUserProfile - name: $name")
            val email = if (response.has(EMAIL)) {
                response.getString(EMAIL)
            } else {
                "ricky@gmail.com"
            }
            Log.d("Facebook", "getUserProfile - email: $email")
            SPController.getInstance(requireActivity()).apply {
                deleteUser()
                saveUser(name, mobile, email, "")
            }
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        request.executeAsync()
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

    companion object {
        lateinit var callbackManager: CallbackManager

        const val PUBLIC_PROFILE = "public_profile"
        const val EMAIL = "email"
        const val FIELDS = "fields"
        const val FIELD_LIST = "id, name, email"
        const val ID = "id"
        const val NAME = "name"
    }
}
