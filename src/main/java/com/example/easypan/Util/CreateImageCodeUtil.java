package com.example.easypan.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CreateImageCodeUtil {
    //图片宽度
    private int width=160;
    //图片高度
    private int height=40;
    //验证码字符个数
    private int codeCount=4;
    //验证码干扰条数
    private int lineCount=20;
    //验证码
    private String code=null;
    //验证码图片
    private BufferedImage bufferedImage=null;


    Random random=new Random();

    public CreateImageCodeUtil(){
        createImage();
    }

    public CreateImageCodeUtil(int width, int height) {
        this.width = width;
        this.height = height;
        createImage();
    }

    public CreateImageCodeUtil(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        createImage();
    }


    public CreateImageCodeUtil(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        createImage();
    }

    //生成图片
    private void createImage(){
        int fontWith=width/codeCount;
        int fontHeight=height - 5;
        int codeY=height - 8;


        //图像
        bufferedImage =new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);

        Graphics graphics = bufferedImage.getGraphics();
//        Graphics2D graphics1 = bufferedImage.createGraphics();
        //
        //bgcolor
        graphics.setColor(getRandColor(200,250));
        graphics.fillRect(0,0,width,height);

        //font
        Font font=new Font("Fixedsys",Font.BOLD,fontHeight);
        graphics.setFont(font);


        //干扰线
        for (int i = 0; i < lineCount; i++) {
            int xs=random.nextInt(width);
            int ys=random.nextInt(height);
            int xe=xs + random.nextInt(width);
            int ye=ys + random.nextInt(height);
            graphics.setColor(getRandColor(1,255));
            graphics.drawLine(xs,ys,xe,ye);
        }

        //噪点
        float yawpRate = 0.01f;
        int area = (int) (yawpRate * width * height);
        for (int j = 0; j < area; j++) {
            int x=random.nextInt(width);
            int y =random.nextInt(height);
            bufferedImage.setRGB(x,y,random.nextInt(255));
        }
        String str1=randomStr(codeCount);
        this.code=str1;
        for (int i = 0; i < codeCount; i++) {
            String strRand=str1.substring(i,i+1);
            graphics.setColor(getRandColor(1,255));
            graphics.drawString(strRand,i * fontWith + 3,codeY);
        }

    }

    //获得随机字符
    private String randomStr(int n){
        String str1="ZXCVBNMASDFGHJKLQWERTYUIOPzxcvbnmasdfghjklqwertyuiop1234567890";
        String str2="";
        int len=str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r=(Math.random()) * len;
            str2=str2 + str1.charAt((int) r);
        }

        return str2;
    }

    //获得随机颜色
    private Color getRandColor(int fc,int bc){
        if(fc > 255){fc=255;}
        if(bc > 255){bc=255;}
        int r=fc +random.nextInt(bc - fc);
        int g=fc +random.nextInt(bc - fc);
        int b=fc +random.nextInt(bc - fc);
        return  new Color(r,g,b);
    }

    //产生随机字体
    private  Font getFont(int size){
        Random random=new Random();
        Font font[]=new Font[5];
        font[0]=new Font("Ravie",Font.PLAIN,size);
        font[1]=new Font("Antique Olive Compact",Font.PLAIN,size);
        font[2]=new Font("Fixedsys",Font.PLAIN,size);
        font[3]=new Font("Wide Latin",Font.PLAIN,size);
        font[4]=new Font("Gill Sans Ultra Bold",Font.PLAIN,size);

        return font[random.nextInt(5)];
    }
    //扭曲方法
    private void shear(Graphics graphics,int w1,int h1,Color color){
        shearX(graphics,w1,h1,color);
        shearY(graphics,w1,h1,color);
    }

    private void shearX(Graphics graphics,int w1,int h1,Color color){

    }
    private void shearY(Graphics graphics,int w1,int h1,Color color){

    }


    public void write(OutputStream outputStream) throws IOException {
        ImageIO.write(bufferedImage,"png",outputStream);
        outputStream.close();
    }
    public BufferedImage getBufferedImage(){
        return bufferedImage;
    }
    public String getCode(){
        return code.toLowerCase();
    }




}
