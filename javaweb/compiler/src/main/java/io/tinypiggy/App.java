package io.tinypiggy;

import io.tinypiggy.parser.OperatorPriority;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String regex="^car";
        String text = "Madagascar acfun acfun";
        Matcher m = Pattern.compile(regex).matcher(text);

        m.useAnchoringBounds(true);
        m.region(7,text.length());
        m.find();
        System.out.println("Matches starting at character "+m.start());

//        regex="\\bcar\\b";
//        m = Pattern.compile(regex).matcher(text);
//        m.useTransparentBounds(false);
//        m.region(7,text.length());
//        m.find();
//        System.out.println("Matches starting at character "+m.start());
    }
}
