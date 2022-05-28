/*
 * Copyright 2022 James Keesey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package pub.carkeys.logparse

import java.awt.Color
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.*
import java.io.File
import javax.swing.JComponent

class FileDropListener(private val parseOptions: ParseOptions, private val component: JComponent) : DropTargetListener {
    private var background: Color = component.background

    override fun drop(event: DropTargetDropEvent) {
        event.acceptDrop(DnDConstants.ACTION_COPY)
        val transferable = event.transferable
        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            val entries = transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<*>
            for (entry in entries) {
                if (entry is File) {
                    parseOptions.files.add(entry)
                }
            }
        }
        event.dropComplete(true)
        component.background = background
        LogParse(parseOptions).process()
        parseOptions.files.clear()
    }

    override fun dragEnter(event: DropTargetDragEvent?) {
        component.background = dropColor
    }

    override fun dragExit(event: DropTargetEvent?) {
        component.background = background
    }

    override fun dragOver(event: DropTargetDragEvent?) {}
    override fun dropActionChanged(event: DropTargetDragEvent?) {}

    companion object {
        private val dropColor = Color(100, 160, 220, 255)
    }
}