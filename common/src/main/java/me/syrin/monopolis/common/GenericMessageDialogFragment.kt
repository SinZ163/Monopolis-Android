package me.syrin.monopolis.common

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_generic_message_layout.view.*
import java.lang.IllegalStateException

class GenericMessageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val myView = it.layoutInflater.inflate(R.layout.fragment_generic_message_layout, null)
            myView.text_view_title.text = arguments?.getString("title")
            myView.text_view_description.text = arguments?.getString("description")
            builder.setView(myView)

            val dialog = builder.create()

            myView.button_continue.setOnClickListener {
                dialog.dismiss()
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}