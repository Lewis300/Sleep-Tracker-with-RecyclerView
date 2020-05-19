/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter(val clickListener: SleepNightListener): ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback())
{
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(getItem(position)!!, clickListener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: SleepNight, clickListener: SleepNightListener)
        {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        // Use this to inflate layout, no clue why we need a companion object
        companion object {
        fun from(parent: ViewGroup): ViewHolder {
             val layoutInflater = LayoutInflater.from(parent.context)
             val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
             return ViewHolder(binding)
            }
        }
    }
}

// Had to make this to override some important equality-like functions
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>()
{
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean
    {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean
    {
        return oldItem == newItem
    }
}

// If youre reading this I want to know exactly what this class does.
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) // what is Unit? What does the -> mean in this case?
{
    // He starts talking about lambda expressions here, don't know what that is
    // I see, the lambda expression is in the class header, it returns nothing.
    // So, how is clickListener set to a value?
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

sealed class DataItem
{
    data class SleepNightItem(val sleepNight: SleepNight): DataItem()
    {
     override val id = sleepNight.nightId
    }

    object Header: DataItem()
    {
     override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}