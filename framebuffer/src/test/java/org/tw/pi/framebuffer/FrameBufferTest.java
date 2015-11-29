package org.tw.pi.framebuffer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Calendar;

import org.junit.Test;

public class FrameBufferTest {
	@Test
	public void testLoad() throws Exception {
		Class.forName(FrameBuffers.class.getName());
	}
	
	private static Point project(Point center, int value, int max, int length) {
		double radians = (value / (double) max) * (2 * Math.PI);
		Point p = new Point(center.x, center.y);
		p.x += length * Math.cos(radians);
		p.y += length * Math.sin(radians);
		return p;
	}
	
	public static void main(String[] args) throws Exception {
		FrameBufferedImage fb = new FrameBufferedImage(args[0]);
		try {
			Graphics2D g = (Graphics2D) fb.getGraphics();
			Dimension dim = new Dimension(fb.getWidth(), fb.getHeight());
			Point center = new Point(dim.width / 2, dim.height / 2);
			int radius = Math.min(dim.width, dim.height) / 2;
			Calendar c = Calendar.getInstance();
			while(true) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, dim.width, dim.height);
				g.setColor(Color.WHITE);
				g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);

				c.setTimeInMillis(System.currentTimeMillis());
				int hour = c.get(Calendar.HOUR);
				int minute = c.get(Calendar.MINUTE);
				int second = c.get(Calendar.SECOND);
				
				Point p = project(center, hour, 12, radius * 2 / 3);
				g.drawLine(center.x, center.y, p.x, p.y);
				
				p = project(center, minute, 60, radius * 3 / 4);
				g.drawLine(center.x, center.y, p.x, p.y);
				
				p = project(center, second, 60, radius * 5 / 6);
				g.drawLine(center.x, center.y, p.x, p.y);
				
				Thread.sleep(500);
			}
		} finally {
			fb.close();
		}
	}
}
