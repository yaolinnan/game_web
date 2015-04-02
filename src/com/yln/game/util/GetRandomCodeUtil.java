package com.yln.game.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class GetRandomCodeUtil {

	public static byte[] getImage(String code){
		int lengh=code.length();
		int fsize=15;//字体大小
		int fwidth=fsize+1;
		int width=fwidth*lengh+6;//图片宽度
		int height=fsize*2+1;//图片高度
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g=image.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		//设置边框颜色
		g.setColor(Color.LIGHT_GRAY);
		//边框字体样式
		g.setFont(new Font("Arial", Font.BOLD, height - 2));
		//绘制边框
		g.drawRect(0, 0, width - 1, height -1);
		//绘制噪点
		Random rand = new Random();
		//设置噪点颜色
		g.setColor(Color.LIGHT_GRAY);
		for(int i = 0;i < lengh * 6;i++){
		int x = rand.nextInt(width);
		int y = rand.nextInt(height);
		//绘制1*1大小的矩形
		g.drawRect(x, y, 1, 1);
		}
		//绘制验证码
		int codeY = height - 10;  
		//设置字体颜色和样式
		g.setColor(new Color(19,148,246));
		g.setFont(new Font("Georgia", Font.BOLD, fsize));
		for(int i = 0; i < lengh;i++){
			g.drawString(String.valueOf(code.charAt(i)), i * 16 + 5, codeY);
		}
		//关闭资源
		g.dispose();
//		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(bos);
		byte[] bts=null;
		try {
		     jpeg.encode(image);
             bts = bos.toByteArray();
//             inputStream = new ByteArrayInputStream(bts);
	         } catch (ImageFormatException e) {
	             e.printStackTrace();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
		return bts;
	}
	
	public static String GetCode(int length){
	    char[] codes={'0','1','2','3','4','5','6','7','8','9',
				'a','b','c','d','e','f','g','h','i',
				 'j','k','l','m','n','o','p','q','r','s','t',
				 'u','v','w','x','y','z','A','B','C',
				 'D','E','F','G','H','I','J','K','L',
				 'M','N','O','P','Q','R','S','T','U','V',
				 'W','X','Y','Z'};
//		codes=Arrays.copyOfRange(codes, 0, length);
		int n=codes.length;
		char[] result=new char[length];
		for(int i=0;i<result.length;i++){
			int r=(int) (Math.random()*n);
			result[i]=codes[r];
		}
		return String.valueOf(result);
	}
}
