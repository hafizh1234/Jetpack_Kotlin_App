package com.projek.jetpack.academies.ui.reader.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projek.jetpack.academies.data.source.local.entity.ModuleEntity
import com.projek.jetpack.databinding.ItemsModuleListCustomBinding

class ModuleListAdapter internal constructor(private val listener: AdapterClickListener): RecyclerView.Adapter<ModuleListAdapter.ModuleViewHolder>() {
    private val listModule=ArrayList<ModuleEntity>()

    fun setModules(modules:List<ModuleEntity>?){
        if(modules==null) return
        this.listModule.clear()
        this.listModule.addAll(modules)
    }
    inner class ModuleViewHolder(private val binding:ItemsModuleListCustomBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(module: ModuleEntity){
            binding.tvModule.text=module.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val binding=ItemsModuleListCustomBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val module=listModule[position]
        holder.bind(module)
        holder.itemView.setOnClickListener{
            listener.onItemClicked(holder.bindingAdapterPosition,listModule[holder.bindingAdapterPosition].moduleId)
        }
    }

    override fun getItemCount(): Int {
        return listModule.size
    }
}

internal interface AdapterClickListener{
    fun onItemClicked(position:Int,moduleId:String)
}