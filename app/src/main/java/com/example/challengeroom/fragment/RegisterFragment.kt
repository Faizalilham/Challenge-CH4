package com.example.challengeroom.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.example.challengeroom.R
import com.example.challengeroom.databinding.FragmentRegisterBinding
import com.example.challengeroom.util.Constant
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private lateinit var sharePref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        sharePref = context?.getSharedPreferences(Constant.SHARE_PREFERENCE, Context.MODE_PRIVATE)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            doRegister()
            toLogin()
        }
    }

    private fun doRegister(){
       binding.btnRegister.setOnClickListener {
           val username = binding.etUsername.text.toString().trim()
           val email = binding.etEmail.text.toString().trim()
           val password = binding.etPassword.text.toString().trim()
           val confirmPassword = binding.etConfirmPassword.text.toString().trim()

           if(username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()){
               if(password.equals(confirmPassword)){
                   sharePref.edit().apply{
                       putString(Constant.USERNAME,username)
                       putString(Constant.EMAIL,email)
                       putString(Constant.PASSWORD,password)
                       putString(Constant.CONFIRM_PASSWORD,confirmPassword)
                       apply()
                   }
                   Navigation.findNavController(binding.root).navigate(R.id.loginFragment)
               }else{
                   setToast(WARNING, NOT_EQUALS,MotionToastStyle.WARNING)
               }
           }else{
               setToast(WARNING, EMPETY,MotionToastStyle.WARNING)
           }
       }
    }

    fun toLogin(){
        binding.tvLogin.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.loginFragment)
        }
    }

    fun setToast(tittle : String,message :String,style : MotionToastStyle){
        MotionToast.createToast(requireActivity(), tittle,
            message,
            style,
            MotionToast.GRAVITY_TOP,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(requireActivity(),R.font.poppins_regular))
    }


    companion object{
        private const val WARNING = "Warning !"
        private const val NOT_EQUALS = "The password and confirm password fields must be the same"
        private const val EMPETY = "Columns cannot be empty"
    }

}