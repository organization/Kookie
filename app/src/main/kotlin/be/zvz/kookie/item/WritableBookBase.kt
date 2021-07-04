/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package be.zvz.kookie.item

import be.zvz.kookie.nbt.tag.CompoundTag
import be.zvz.kookie.nbt.tag.ListTag
import be.zvz.kookie.nbt.tag.StringTag

open class WritableBookBase(identifier: ItemIdentifier, name: String) : Item(identifier, name) {
    override val maxStackSize: Int = 1

    var pages: MutableList<WritableBookPage> = mutableListOf()

    fun pageExists(pageId: Int): Boolean = pageId < pages.size
    fun getPageText(pageId: Int): String = pages[pageId].text
    fun setPageText(pageId: Int, pageText: String) = apply {
        if (!pageExists(pageId)) addPage(pageId)
        pages[pageId] = WritableBookPage(pageText)
    }

    fun addPage(pageId: Int) = apply {
        if (pageId < 0) throw IllegalArgumentException("Page number \"$pageId\" is out of range")
        for (current in pages.size until pageId) pages[current] = WritableBookPage("")
    }

    fun deletePage(pageId: Int) = apply {
        pages.removeAt(pageId)
    }

    fun insertPage(pageId: Int, pageText: String) = apply {
        if (pageId < 0 || pageId > pages.size) throw IllegalArgumentException("Page ID must not be negative")
        pages.add(pageId, WritableBookPage(pageText))
    }

    fun swapPages(pageId1: Int, pageId2: Int): Boolean {
        val pageContents1 = getPageText(pageId1)
        val pageContents2 = getPageText(pageId2)
        setPageText(pageId1, pageContents2)
        setPageText(pageId2, pageContents1)

        return true
    }

    protected override fun deserializeCompoundTag(tag: CompoundTag) {
        super.deserializeCompoundTag(tag)
        this.pages.clear()

        val pages = tag.getListTag(TAG_PAGES)
        pages?.value?.forEach {
            if (it is CompoundTag) this.pages.add(WritableBookPage(it.getString(TAG_PAGE_TEXT), it.getString(TAG_PAGE_PHOTONAME)))
            else if (it is StringTag) this.pages.add(WritableBookPage(it.value))
        }
    }

    protected override fun serializeCompoundTag(tag: CompoundTag) {
        super.serializeCompoundTag(tag)
        if (this.pages.size > 0) {
            val pages = ListTag<CompoundTag>()
            this.pages.forEach {
                pages.push(
                    CompoundTag.create()
                        .setString(TAG_PAGE_TEXT, it.text)
                        .setString(TAG_PAGE_PHOTONAME, it.photoName)
                )
            }
            tag.setTag(TAG_PAGES, pages)
        } else {
            tag.removeTag(TAG_PAGES)
        }
    }

    companion object {
        const val TAG_PAGES = "pages"
        const val TAG_PAGE_TEXT = "text"
        const val TAG_PAGE_PHOTONAME = "photoname"
    }
}
