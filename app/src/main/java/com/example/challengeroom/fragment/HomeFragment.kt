package com.example.challengeroom.fragment


import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengeroom.R
import com.example.challengeroom.adapter.NoteAdapter
import com.example.challengeroom.databinding.FragmentHomeBinding
import com.example.challengeroom.model.Note
import com.example.challengeroom.room.SetupRoom
import com.example.challengeroom.viewmodel.NoteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception

class HomeFragment : Fragment() {


    private lateinit var binding : FragmentHomeBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private  var listData = mutableListOf<Note>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentHomeBinding.inflate(layoutInflater)
       noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycler()
//        val info = resources.getString(R.string.toast_info)
//        val infoMessage = resources.getString(R.string.toast_info_message)
//        noteViewModel.data.value?.size ?: setToast(info, infoMessage,MotionToastStyle.INFO)
        noteViewModel.data.observe(requireActivity()){
            noteAdapter.submitData(it)
        }
        addItem()
        doLogout()
    }


    private fun setRecycler(){
        noteAdapter = NoteAdapter(object : NoteAdapter.Clicked{
            override fun onClick(note: Note) {
                val args = HomeFragmentDirections.actionHomeFragmentToDetailFragment(note)
                findNavController().navigate(args)
            }

            override fun onUpdate(note: Note) {
                val updatetittle = resources.getString(R.string.alert_update_tittle)
                val tittleSuccessToast = resources.getString(R.string.success)
                val tittleErrorToast = resources.getString(R.string.error)
                val updateSuccessMessage = resources.getString(R.string.toast_success_update_data)
                val updateErrorMessage = resources.getString(R.string.toast_error_update_data)
                try{
                    noteViewModel.updateAlert(requireActivity(),note.id,layoutInflater,updatetittle,tittleSuccessToast,updateSuccessMessage,MotionToastStyle.SUCCESS)
                }catch (e : Exception){
                    noteViewModel.updateAlert(requireActivity(),note.id,layoutInflater,updatetittle,tittleErrorToast,updateErrorMessage,MotionToastStyle.ERROR)
                }
            }

            override fun onDelete(note: Note) {
                val deleteWarning = resources.getString(R.string.alert_warning_delete)
                val deleteMessage = resources.getString(R.string.alert_message_delete_data)
                val cancelText = resources.getString(R.string.alert_cancel)
                val deleteText = resources.getString(R.string.alert_delete)
                val deleteSuccessMessage = resources.getString(R.string.toast_success_delete_data)
                val deleteErrorMessage = resources.getString(R.string.toast_error_delete_data)
                val tittleSuccessToast = resources.getString(R.string.success)
                val tittleErrorToast = resources.getString(R.string.error)

                try {
                    noteViewModel.deleteAlert(requireActivity(),note,deleteText,cancelText,deleteWarning,deleteMessage,tittleSuccessToast,deleteSuccessMessage,MotionToastStyle.SUCCESS)
                }catch (e: Exception){
                    noteViewModel.deleteAlert(requireActivity(),note,deleteText,cancelText,deleteWarning,deleteMessage,tittleErrorToast,deleteErrorMessage,MotionToastStyle.ERROR)
                }
            }

        })

        binding.noteRecycler.apply {
            adapter = noteAdapter
            layoutManager = if(context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager(requireActivity(),2)
            }else{
                LinearLayoutManager(requireActivity())
            }
        }
    }

    private fun addItem(){
        binding.LinearAddItem.setOnClickListener {
            val toastSuccess = resources.getString(R.string.toast_success_add_data)
            val toastError = resources.getString(R.string.toast_success_add_data)
            val tittleSuccessToast = resources.getString(R.string.success)
            val tittleErrorToast = resources.getString(R.string.error)
            val tittleAlertAdd = resources.getString(R.string.alert_add_tittle)
           try{
               noteViewModel.addAlert(requireActivity(),layoutInflater,tittleAlertAdd,tittleSuccessToast,toastSuccess,MotionToastStyle.SUCCESS)
           }catch(e : Exception){
               noteViewModel.addAlert(requireActivity(),layoutInflater,tittleAlertAdd,tittleErrorToast,toastError,MotionToastStyle.ERROR)
           }
        }
    }

    private fun doLogout(){
        binding.linearLogout.setOnClickListener {
            val cancelText = resources.getString(R.string.alert_cancel)
            val logoutText = resources.getString(R.string.alert_logout)
            val logoutWarning = resources.getString(R.string.alert_warning_delete)
            val logoutMessage = resources.getString(R.string.alert_logout)
            noteViewModel.logoutAlert(requireActivity(),binding.root,logoutText,cancelText,logoutWarning,logoutMessage)

        }
    }
}