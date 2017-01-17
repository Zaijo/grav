package gui;

import java.awt.Dimension;
import java.awt.Graphics;


public abstract class WindowStrategy {
	/* 
	kazda strategia zorazenia musi mat
		vystup
			graphics -- plocha kam sa bude kreslit
			update -- vykreslenie pripravenych zmien (Double Buffering)
			start
			rozmery
	*/
	public Graphics g;
	public abstract void update();
	public abstract void start();
	public abstract Dimension getDim();
}
