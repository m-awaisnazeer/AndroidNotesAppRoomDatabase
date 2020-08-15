package com.communisolve.notesappusingroomdatabase.ui

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.Navigation
import com.communisolve.notesappusingroomdatabase.R
import com.communisolve.notesappusingroomdatabase.db.Note
import com.communisolve.notesappusingroomdatabase.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch


class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)

        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            edit_text_note.setText(note!!.note)
            edit_text_title.setText(note!!.title)

        }

        save_btn.setOnClickListener({ view ->
            val noteTitle = edit_text_title.text.toString().trim()
            val noteBody = edit_text_note.text.toString().trim()

            if (noteTitle.isEmpty()) {
                edit_text_title.error = "title required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                edit_text_note.error = "note required"
                edit_text_note.requestFocus()
                return@setOnClickListener
            }

            launch {


                context?.let {

                    val mnote = Note(
                        noteTitle
                        , noteBody
                    )

                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(mnote)
                        it.toast("Note Save")
                    } else {
                        mnote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mnote)
                        it.toast("Note updated")
                    }

                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }
            }


        })

    }

    private fun deleteNote() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you Sure?")
            setMessage("You cannot undo this operation")

            setPositiveButton("yes"){ _, _ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)

                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
            setNegativeButton("No"){_,_ ->

            }

        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete -> if (note != null) deleteNote() else context?.toast("Cannot Delete")
        }

        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu, menu)
    }
}