package org.dotahq.util

import java.awt.datatransfer.DataFlavor;

class DragAndDropUtil {
	
	public static DataFlavor createDataFlavor(Object data){
		return createDataFlavor(data.getClass())
	}

    public static DataFlavor createDataFlavor(Class dataClass){
        return new DataFlavor("${DataFlavor.javaJVMLocalObjectMimeType}; class=${dataClass.canonicalName}")
    }
}
