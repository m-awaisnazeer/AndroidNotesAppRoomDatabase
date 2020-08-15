package com.communisolve.notesappusingroomdatabase.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.communisolve.notesappusingroomdatabase.R
import com.communisolve.notesappusingroomdatabase.db.Note
import kotlinx.android.synthetic.main.note_layout.view.*

class NoteAdapter( private val notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.view.text_view_note.text = "Note:\n"+notes[position].note
        holder.view.text_view_title.text = "Title:"+notes[position].title

        holder.view.setOnClickListener {

            val action = HomeFragmentDirections.actionAddNote()
            action.note= notes[position]

            Navigation.findNavController(it).navigate(action)
        }
    }
}