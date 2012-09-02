package org.dotahq.util

import java.awt.Component

import javax.swing.JOptionPane

class DisplayUtils {
	static void error(Throwable th, Component parentComponent = null) {
		th.printStackTrace()
		StringBuilder msg = new StringBuilder()
		msg << th.getLocalizedMessage()
		JOptionPane.showMessageDialog(parentComponent, msg, "Error", JOptionPane.ERROR_MESSAGE)
	}
	
	static void warning(Throwable th, Component parentComponent = null) {
		th.printStackTrace()
		StringBuilder msg = new StringBuilder()
		msg << th.getLocalizedMessage()
		JOptionPane.showMessageDialog(parentComponent, msg, "Error", JOptionPane.WARNING_MESSAGE)
	}
	
	static void warning(String msg, Component parentComponent = null) {
		JOptionPane.showMessageDialog(parentComponent, msg, "Warning", JOptionPane.WARNING_MESSAGE)
	}
	
	static void message(String msg, String title, Component parentComponent = null) {
		JOptionPane.showMessageDialog(parentComponent, msg, title, JOptionPane.ERROR_MESSAGE)
	}
}
