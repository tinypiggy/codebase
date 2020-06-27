package io.tinypiggy;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        String regex="^car";
//        String text = "Madagascar acfun acfun";
//        Matcher m = Pattern.compile(regex).matcher(text);
//
//        m.useAnchoringBounds(true);
//        m.region(7,text.length());
//        m.find();
//        System.out.println("Matches starting at character "+m.start());

//        regex="\\bcar\\b";
//        m = Pattern.compile(regex).matcher(text);
//        m.useTransparentBounds(false);
//        m.region(7,text.length());
//        m.find();
//        System.out.println("Matches starting at character "+m.start());

//        long start = System.currentTimeMillis();
//        fib(36);
//        System.out.println(System.currentTimeMillis() - start);

        List<String> names = new ArrayList<>();
        names.add(2, "liuke");
        System.out.println(names.get(2));
    }

    private static int fib(int x){
        if (x < 2){
            return 1;
        }
        return fib(x-1) + fib(x-2);
    }
}
