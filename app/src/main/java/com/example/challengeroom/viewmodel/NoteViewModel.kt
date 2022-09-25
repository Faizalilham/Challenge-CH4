package com.example.challengeroom.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.challengeroom.R
import com.example.challengeroom.databinding.AddAlertBinding
import com.example.challengeroom.model.Note
import com.example.challengeroom.room.Dao
import com.example.challengeroom.room.SetupRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class NoteViewModel(application: Application) : AndroidViewModel(application) {

    var data : LiveData<MutableList<Note>>
    var dao: Dao = SetupRoom.buildDatabase(application).dao()

    init{
        data = dao.readData()
    }

    private fun addUser(note : Note){
        viewModelScope.launch(Dispatchers.IO){
            dao.createData(note)
        }
    }

    private fun updateDatas(note : Note){
        viewModelScope.launch(Dispatchers.IO){
            dao.updateData(note)
        }
    }

    private fun deleteData(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteData(note)
        }
    }

    fun addAlert(context: Context ,inflater : LayoutInflater,tittle : String,
                 tittleToast : String,message : String,style : MotionToastStyle){
            val binding = AddAlertBinding.inflate(inflater)
            val alert = AlertDialog.Builder(context).create()
            alert.apply {
                setTitle(tittle)
                setView(binding.root)
            }
            binding.btnSave.setOnClickListener {
                val tittle = binding.etTittle.text.toString().trim()
                val description = binding.etDescription.text.toString().trim()
                if(tittle.isNotBlank() && description.isNotBlank()){
                    addUser(Note(0,tittle,description))
                    setToast(context,tittleToast,message,style)
                    alert.dismiss()
                }else{
                    setToast(context,"Error","Data Cannot be Empety",MotionToastStyle.ERROR)
                }
            }
            alert.show()
    }

    fun updateAlert(context: Context,id : Int,inflater : LayoutInflater,tittle : String,
                    tittleToast : String,message : String,style : MotionToastStyle){
        val binding = AddAlertBinding.inflate(inflater)
        val alert = AlertDialog.Builder(context).create()
        alert.apply {
            setTitle(tittle)
            setView(binding.root)
        }
       CoroutineScope(Dispatchers.IO).launch {
          withContext(Dispatchers.Main){
              val datas = dao.readDataById(id)
              if(datas.size > 0){
                  binding.etTittle.setText(datas[0].tittle)
                  binding.etDescription.setText(datas[0].description)
              }else{
                  Toast.makeText(context, "$datas", Toast.LENGTH_SHORT).show()
              }
          }
       }
        binding.btnSave.setOnClickListener {
            val tittles = binding.etTittle.text.toString().trim()
            val descriptions = binding.etDescription.text.toString().trim()
            if(tittles.isNotBlank() && descriptions.isNotBlank()){
                Log.d("UPDATE_DATA", Note(0,tittles,descriptions).toString())
                updateDatas(Note(id,tittles,descriptions))
                setToast(context,tittleToast,message,style)
                alert.dismiss()
            }else{
                setToast(context,"Error","Data Cannot be Empety",MotionToastStyle.ERROR)
            }
        }

        alert.show()

    }

    fun deleteAlert(context: Context,note :Note,deleteText : String,cancelText : String,tittleAlert : String,messageAlert : String,
                    tittleToast : String,message : String,style : MotionToastStyle){
        val alert = AlertDialog.Builder(context)
        alert.apply {
            setTitle(tittleAlert)
            setMessage(messageAlert)
            setIcon(R.drawable.ic_baseline_warning_24)
            setPositiveButton(deleteText){ dialogInterface: DialogInterface, i: Int ->
               deleteData(note)
                setToast(context,tittleToast,message,style)
            }
            setNegativeButton(cancelText){ dialogInterface: DialogInterface, i: Int -> }
        }
        alert.show()

    }

    fun setToast(context : Context,tittle : String,message :String,style : MotionToastStyle){
        MotionToast.createToast(context as Activity, tittle,
            message,
            style,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(context, R.font.poppins_regular))
    }

}