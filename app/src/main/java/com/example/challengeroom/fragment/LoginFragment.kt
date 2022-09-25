package com.example.challengeroom.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.example.challengeroom.R
import com.example.challengeroom.databinding.FragmentLoginBinding
import com.example.challengeroom.util.Constant
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class LoginFragment : Fragment() {


    private lateinit var binding : FragmentLoginBinding
    private lateinit var sharePref : SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentLoginBinding.inflate(layoutInflater)
        sharePref = context?.getSharedPreferences(Constant.SHARE_PREFERENCE, Context.MODE_PRIVATE)!!
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doLogin()
        toRegister()

    }

    private fun doLogin(){
       binding.btnLogin.setOnClickListener {
           val email= binding.etEmail.text.toString().trim()
           val password = binding.etPassword.text.toString().trim()
           val emails = sharePref.getString(Constant.EMAIL,Constant.UNDEFINED)
           val passwords = sharePref.getString(Constant.PASSWORD,Constant.UNDEFINED)
           val username = sharePref.getString(Constant.USERNAME,Constant.UNDEFINED)
           if(email.isNotBlank() && password.isNotBlank()){
               if(email.equals(emails) && password.equals(passwords)){
                   setToast(SUCCESS, "$SUCCESS_MESSAGE ,Hai $username",MotionToastStyle.SUCCESS)
                   Navigation.findNavController(binding.root).navigate(R.id.homeFragment)
               }else{
                  setToast(ERROR, ERROR_MESSAGE,MotionToastStyle.ERROR)
               }
           }else{
               setToast(WARNING, WARNING_MESSAGE,MotionToastStyle.WARNING)
           }
       }
    }

    private fun toRegister(){
        binding.tvRegister.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.registerFragment)
        }
    }

    fun setToast(tittle : String,message :String,style : MotionToastStyle){
        MotionToast.createToast(requireActivity(), tittle,
            message,
            style,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(requireActivity(),R.font.poppins_regular))
    }

    companion object{
        private const val SUCCESS = "Success"
        private const val WARNING = "Warning !"
        private const val ERROR = "Error"
        private const val SUCCESS_MESSAGE = "Hurray Login success"
        private const val ERROR_MESSAGE = "Your email or password is wrong"
        private const val WARNING_MESSAGE = "Columns cannot be empty"

    }


}