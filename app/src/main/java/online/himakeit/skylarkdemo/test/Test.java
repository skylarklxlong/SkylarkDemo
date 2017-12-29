package online.himakeit.skylarkdemo.test;

/**
 * @author：LiXueLong
 * @date:2017/12/29
 * @mail1:skylarklxlong@outlook.com
 * @mail2:li_xuelong@126.com
 * @des:
 */
public class Test {
    public static void main(String[] args) {

        System.out.println("i++");
        int i = 3;
        /**
         * count=3+4+5
         */
        int count = (i++) + (i++) + (i++);
        /**
         * i=6
         */
        System.out.println(i);
        /**
         * count=12
         */
        System.out.println(count);

        System.out.println("++i");
        int j = 3;
        /**
         * count=4+5+6
         */
        count = (++j) + (++j) + (++j);
        /**
         * j=6
         */
        System.out.println(j);
        /**
         * count=15
         */
        System.out.println(count);
    }
}
