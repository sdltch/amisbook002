package com.example.amisbook002;
import java.text.DecimalFormat;
import java.util.stream.IntStream;

/*
* 平帐计算
* 集装箱号校验规则
*
* */
public class NewTest {
    public static void main(String[] args){
        NewTest.pzjs();
       // NewTest.jzxjs();
    }
    //平帐计算
    public static void pzjs(){
        //数量
        double b=6;
        //净耗
        double c= 23.2381;
        //有形损耗%-无形损耗%
        double d=3;
        double e=0;
        double dd = d/100;
        double ee = e/100;
        double ss = 1-dd-ee;
        double cc = c/ss;
        System.out.println("cc除法不取位数="+cc);
        DecimalFormat decimalFormat = new DecimalFormat("#.00000");
        String format = decimalFormat.format(cc);
        double v = Double.parseDouble(format);
        System.out.println("除法不取位数="+cc);
        System.out.println("除法取5位小数="+v);
        //除法不取位数
        double bb = b*cc;
        //除法取小数后5位
        double bbb = b*v;
        System.out.println("——————bb除法不取位数计算结果:"+bb+"-----bbb除法取小数后5位计算结果："+bbb);
        String format1 = decimalFormat.format(bb);
        String format2 = decimalFormat.format(bbb);
        System.out.println("format1除法不取位数结果取5位小数:"+format1+"-----format2除法取小数后5位结果取5位小数："+format2);
//        float i=1f,j=3f;
//        double k=1,x=3;
//        float df = i/j;
//        double dfa=k/x;
//        System.out.println(df+"dd"+dfa);
    }
    //集装箱号
    public static void jzxjs(){
        String ss = "QWWE1433422";
        String[] strone = {"A","","B","C","D","E","F","G","H","I","J","K",
                                   "","L","M","N","O","P","Q","R","S","T","U",
                                   "","V","W","X","Y","Z"};
        int powjx=0;
        System.out.println("subone的长度："+ss.length());
        for(int dd=0; dd<ss.length()-1; dd++){
            char subss = ss.charAt(dd);
            String subone = String.valueOf(subss);
            int dsaf = dd+1;
            //System.out.println("subone的："+dd+"位："+subone);
            boolean matches = subone.matches("[0-9]+");
            System.out.println("subone111的第："+dsaf+"位："+matches);
            if (matches == true){
                //System.out.println("subone111的："+dd+"位："+matches);
                int jzxone = Integer.parseInt(subone);
                //System.out.println("intsubone="+jzxone);
                //2的dd次方
                double pow = Math.pow(2,dd);
                powjx += pow * jzxone;
                System.out.println("集装箱号："+ss+"的次方："+pow);
                System.out.println("集装箱号："+ss+"的第："+dsaf+"位的运算对应符为："+jzxone+"；值为："+jzxone*pow);
            }else if(matches == false){
                //System.out.println("subone222的："+dd+"位："+matches);
                for (int aa=0; aa<strone.length; aa++){
                    String s = strone[aa];
                    //System.out.println("aa111="+s);
                    if (s.equals(subone)){
                        int jsone = aa + 10;
                        //System.out.println("dsaf="+dsaf);
                        //2的dd次方
                        double pow = Math.pow(2, dd);
                        powjx += jsone * pow;
                        System.out.println("集装箱号："+ss+"的次方："+pow);
                        System.out.println("集装箱号："+ss+"的第："+dsaf+"位的运算对应符为："+jsone+"；值为："+jsone*pow);
                    }
                }
            }
            System.out.println("第"+(dd+1)+"位，="+powjx);
        }
        //对前10位取模
        double qmjs = powjx%11;
        int qmjs1 = (int) qmjs;
        System.out.println("对"+ss+"前10位取模="+qmjs1);
        char subthree = ss.charAt(10);
        //char转换int
        int chars = Integer.parseInt(String.valueOf(subthree));
        System.out.println("集装箱号："+ss+"，第11位值chars="+chars);
        if(qmjs1 == chars){
            System.out.println(ss+"==pass");
        }else {
            System.out.println(ss+"==fail");
        }
    }

}