package com.lnragisoft.justtest;

public class Test {
	public static void main(String[] args) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				System.out.println(info.getClassName());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
