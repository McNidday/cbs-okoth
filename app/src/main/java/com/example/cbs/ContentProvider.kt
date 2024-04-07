package com.example.cbs

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri

class ContentProvider : ContentProvider() {

    companion object {
        // FOR DATA
        private const val AUTHORITY = "com.example.contentprovider.cp"
        private val TABLE_NAME: String = "NAMES"
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    override fun onCreate(): Boolean {
        // Initialize StudentDB
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        // Fetch student records from student room db.
        if (context != null) {
            val adminNo = ContentUris.parseId(uri)
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String? = uri.encodedPath

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Insert student record into student room DB
        if (context != null) {
            val db = (context?.applicationContext as StudentApp).database
            val id = values?.let {
                db.getDAO()
                    .syncUpsert(
                        it
                    )
            }
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return id?.let { ContentUris.withAppendedId(uri, it) }
            }
        }
        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        // Remove student record from student room db
        if (context != null) {
            val db = (context?.applicationContext as StudentApp).database
            val count = db.getDAO().deleteWithCount(ContentUris.parseId(uri))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into $uri")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?
    ): Int {
        // Update student record in student room db.
        if (context != null) {
            val db = (context?.applicationContext as StudentApp).database
            val id = values?.let {
                db.getDAO()
                    .syncUpsert(
                        it
                    )
            }
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
//                return ContentUris.withAppendedId(uri, id)
                return 0
            }
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }
}