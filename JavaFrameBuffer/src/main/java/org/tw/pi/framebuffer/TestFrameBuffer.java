/*
 *	This file is the JNI Java part of a Raspberry Pi FrameBuffer project.
 *
 *	Created 2013 by Thomas Welsch (ttww@gmx.de).
 *
 *	Do whatever you want to do with it :-)
 *
 **/
package org.tw.pi.framebuffer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Simple test file class for demonstrate the FrameBuffer class.
 */
public class TestFrameBuffer {

	private FrameBuffer fb;

	// -----------------------------------------------------------------------------------------------------------------

	public TestFrameBuffer(String deviceName) {
		fb = new FrameBuffer(deviceName);
	}

	// -----------------------------------------------------------------------------------------------------------------

	private void startTests() {

		new Thread("Test") {
			@Override
			public void run() {
				BufferedImage img = fb.getScreen();

				int w = img.getWidth();
				int h = img.getHeight();

				Graphics2D g = img.createGraphics();

				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, w, h);

				g.setColor(Color.WHITE);
				g.drawString("Hello world !", 22, 45);

				int y = 17;
				g.setColor(Color.RED);
				g.fillRect(0, y, 20,20);
				y += 21;

				g.setColor(Color.GREEN);
				g.fillRect(0, y, 20,20);
				y += 21;

				g.setColor(Color.BLUE);
				g.fillRect(0, y, 20,20);
				y += 21;

				AffineTransform st = g.getTransform();
				g.translate(w/2, h/2+5);

				for (int i=0; i<360; i += 4) {

					g.rotate(Math.toRadians(i));

					g.setColor(Color.WHITE);
					g.drawString("Nice !!!", 0,0);

					try {
						sleep(150);
					} catch (InterruptedException e) {
						return;
					}

					g.setColor(Color.LIGHT_GRAY);
					g.drawString("Nice !!!", 0,0);

					g.rotate(Math.toRadians(-i));
				}
				g.setTransform(st);

				Random r = new Random();

				while (true) {
					int x1 = r.nextInt(w);
					int x2 = r.nextInt(w);
					int y1 = r.nextInt(h);
					int y2 = r.nextInt(h);

					g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}.start();
	}

	// -----------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				TestFrameBuffer mt = new TestFrameBuffer("/dev/fb1");
//				TestFrameBuffer mt = new TestFrameBuffer("dummy_200x330");

				if (true) {
					JFrame f = new JFrame("Frame Buffer Test");
					f.setSize(400, 400);
					f.setLocation(300,200);
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.getContentPane().add(BorderLayout.CENTER, mt.fb.getScreenPanel());
					f.setVisible(true);
				}

				mt.startTests();
			}
		});
	}

}	// of TestFrameBuffer
