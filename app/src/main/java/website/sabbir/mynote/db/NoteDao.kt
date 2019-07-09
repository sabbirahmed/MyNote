package website.sabbir.mynote.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Insert
    fun addNote(note: Note)

    @Insert
    fun addMultipleNotes(vararg note: Note)

    @Query("SELECT * FROM note")
    fun getAllNotes() : List<Note>

}