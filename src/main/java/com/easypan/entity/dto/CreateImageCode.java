package com.easypan.entity.dto;


import javafx.scene.transform.Shear;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;

public class CreateImageCode {
    //图片宽度
    private  int width=160;
    //图片高度
    private  int height=40;
    //验证码字符个数
    private  int codeCount=4;
    //验证码干扰个数
    private  int lineCount=40;
    //验证码
    private  String  code=null;
    //验证码图片buffer
    private BufferedImage buffImg=null;
    Random random=new Random();

    public CreateImageCode() {
        createImage();
    }

    public CreateImageCode(int width, int height) {
        this.width = width;
        this.height = height;
        createImage();
    }

    public CreateImageCode(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        createImage();
    }

    public CreateImageCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        createImage();
    }

    public CreateImageCode(int width, int height, int codeCount, int lineCount, String code) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.code = code;
        createImage();
    }
    private void createImage(){
        int fontWidth=width/codeCount;
        int fontWHeight=height-5;
        int codeY=height-8;
        //图像buffer
        buffImg=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g=buffImg.getGraphics();
        //设置背景色
        g.setColor(getRandColor(200,250));
        g.fillRect(0,0,width,height);
        Font font =new Font("Fixedays",Font.BOLD,fontWHeight);
        g.setFont(font);
        //设置干扰线
        for(int i=0;i<lineCount;i++){
            int xs=random.nextInt(width);
            int ys=random.nextInt(height);
            int xe=xs+random.nextInt(width);
            int ye=ys+random.nextInt(height);
            g.setColor(getRandColor(1,255));
            g.drawLine(xs,ys,xe,ye);
        }
        //添加噪点
        float yawpRate=0.01f;
        int area=(int) (yawpRate*width*height);
        for(int i=0;i<area;i++){
            int x=random.nextInt(width);
            int y=random.nextInt(height);
            buffImg.setRGB(x,y,random.nextInt(255));
        }
        String str1=randomStr(codeCount);
        this.code =str1;
        for (int i = 0; i < codeCount; i++) {
            String strRand=str1.substring(i,i+1);
            g.setColor(getRandColor(1,255));
            g.drawString(strRand,i* fontWidth+3,codeY);
        }
    }
    private  String randomStr(int n){
        String str1="ASDFGHJKLZXCVBNMQWERTYUIOP1234567890";
        String str2="";
        int len=str1.length()-1;
        double r;
        for (int i = 0; i < n; i++) {
            r=(Math.random())*len;
            str2=str2+str1.charAt((int) r);
        }
        return  str2;
    }
    private Color getRandColor(int fc,int bc){
        if(fc>255) fc=255;
        if(bc>255) bc=255;
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);
        return new Color(r,g,b);
    }
    private Font getFont(int size){
        Random random=new Random();
        Font[] font =new Font[5];
        font[0]=new Font("Ravie",Font.PLAIN,size);
        font[1]=new Font("Antique Olive Compact",Font.PLAIN,size);
        font[2]=new Font("Fixedsys",Font.PLAIN,size);
        font[3]=new Font("Wide Latin",Font.PLAIN,size);
        font[4]=new Font("Gill Sans Ultra Bold",Font.PLAIN,size);
        return font[random.nextInt(5)];
    }
    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg,"png",sos);
        sos.close();
    }
    public String getCode(){return code.toLowerCase();}






}
