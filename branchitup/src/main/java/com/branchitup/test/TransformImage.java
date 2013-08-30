/*
 * %W% %E%
 *
 * YBLOB PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.test;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TransformImage extends JPanel {
	private static final long serialVersionUID = 1L;
	Image scaled;

	   public TransformImage() {
		   transform2();
	   }
	   
	   protected void transform1(){
	       BufferedImage image = getImage("/yblobenv/uploads/images/google_g.png");
	       AffineTransform scaleTransform = new AffineTransform();
//	       int w = image.getWidth();
//	       int h = image.getHeight();
	       // last-in-first-applied: rotate, scale, shear
	       // scaleTransform.shear(0.0, 0.0);
	       scaleTransform.scale(0.1, 0.1);
//	       scaleTransform.rotate(-Math.PI / 2, w / 2, h / 2);
	       AffineTransformOp scaleOp = new AffineTransformOp(
	           scaleTransform, AffineTransformOp.TYPE_BILINEAR);
	       scaled = scaleOp.filter(image, null);
//	       setPreferredSize(new Dimension(scaled.getWidth(), scaled.getHeight()));
	   }
	   
	   private void transform2(){
		   BufferedImage image = getImage("/yblobenv/uploads/images/google_g.png");
		   scaled = image.getScaledInstance(50, 50, Image.SCALE_REPLICATE);//SCALE_SMOOTH
	   }

	   private BufferedImage getImage(String name) {
	       BufferedImage image = null;
	       try {
	           image = ImageIO.read(new File(name));
	       } catch (IOException ioe) {
	           ioe.printStackTrace(System.err);
	       }
	       return image;
	   }

	   public void paintComponent(Graphics g) {
	       final Graphics2D g2d = (Graphics2D)g;
	       g2d.setRenderingHint(
	           RenderingHints.KEY_ANTIALIASING,
	           RenderingHints.VALUE_ANTIALIAS_ON);
	       g2d.drawImage(scaled, 0, 0, null);
	   }

	   public static void main(String[] args) {
	       EventQueue.invokeLater(new Runnable() {
	           public void run() {
	               JFrame f = new JFrame();
	               f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	               f.add(new TransformImage());
	               f.pack();
	               f.setVisible(true);
	           }
	       });
	   }
	}