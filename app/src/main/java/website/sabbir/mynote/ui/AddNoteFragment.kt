package website.sabbir.mynote.ui


import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch

import website.sabbir.mynote.R
import website.sabbir.mynote.db.Note
import website.sabbir.mynote.db.NoteDatabase

class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            titleEditText.setText(note?.title)
            bodyEditText.setText(note?.note)
        }

        saveButton.setOnClickListener { view ->

            val noteTitle = titleEditText.text.toString().trim()
            val noteBody = bodyEditText.text.toString().trim()

            if (noteTitle.isEmpty()) {
                titleEditText.error = "Title required"
                titleEditText.requestFocus()
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                bodyEditText.error = "Note required"
                bodyEditText.requestFocus()
                return@setOnClickListener
            }


            launch {
                context?.let {
                    val mNote = Note(noteTitle, noteBody)

                    if (note == null){
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved")
                    }else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note Updated")
                    }

                    val action = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }

        }
    }

    private fun deleteNote(){
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You connot undo this operation")
            setPositiveButton("Yes") {_, _ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment()
                    Navigation.findNavController(view!!).navigate(action)
                }
            }
            setNegativeButton("No") {_, _ ->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> if (note != null) deleteNote() else context?.toast("Cannot Delete")
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }
}
