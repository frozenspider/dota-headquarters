package org.dotahq.ui

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

import org.dotahq.util.DragAndDropUtil

class DataTransferrable implements Transferable {
	private final Object data
	
	public DataTransferrable(Object data){
		this.data = data
	}
	
	public DataFlavor[] getTransferDataFlavors(){
		def result = new DataFlavor[1]
		result[0] = DragAndDropUtil.createDataFlavor(data)
		return result
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor){
		return flavor == DragAndDropUtil.createDataFlavor(data)
	}
	
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (flavor != DragAndDropUtil.createDataFlavor(data)) {
			throw new UnsupportedFlavorException()
		}
		return data
	}
}