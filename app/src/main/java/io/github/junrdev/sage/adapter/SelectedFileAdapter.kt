package io.github.junrdev.sage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.github.junrdev.sage.R
import io.github.junrdev.sage.model.SelectedItem

private const val TAG = "SelectedFileAdapter"

class SelectedFileAdapter(
    val context: Context,
    val selectedFiles: MutableList<SelectedItem>,
    private val onEdit : (position : Int) -> Unit
) : RecyclerView.Adapter<SelectedFileAdapter.VH>() {


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title = itemView.findViewById<TextView>(R.id.fileName)
        private val remove = itemView.findViewById<ImageView>(R.id.removeFile)
        private val recycle = itemView.findViewById<ImageView>(R.id.removeFile)
        private val item = itemView.findViewById<CardView>(R.id.fileItem)

        fun bind(selectedItem: SelectedItem, onRemove: () -> Unit, onEdit: () -> Unit) {
            title.text = selectedItem.fname
            var deleteNotify = false
            remove.setOnClickListener {

                deleteNotify = true
                remove.visibility = View.GONE
                recycle.visibility = View.VISIBLE

                Toast.makeText(context, "File will be removed after 3 seconds. You can recycle until then", Toast.LENGTH_SHORT).show()

                recycle.setOnClickListener {
                    deleteNotify = false
                    remove.visibility = View.VISIBLE
                    recycle.visibility = View.GONE
                }


                android.os.Handler().postDelayed({
                    if (deleteNotify)
                        onRemove()
                },2500)
            }

            item.setOnClickListener { onEdit() }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.selectedfileitem, parent, false))

    override fun getItemCount(): Int = selectedFiles.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(selectedFiles[position], {
            selectedFiles.remove(selectedFiles[position])
            notifyItemRemoved(position)
        }) {
            onEdit(position)
        }
    }
}