import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class Main {  
    public static boolean match (String input, Pattern regex) {
        // THIS IS THE KEY LINE
        return regex.matcher(input).find();
        // basic strings can match also, but not using a Pattern
        // return input.matches(regex);
    }

    private static void testMatches (String[] tests, List<Map.Entry<String, Pattern>> patterns) {
        for (String s : tests) {
            boolean matched = false;
            for (Map.Entry<String, Pattern> p : patterns) {
                if (match(s, p.getValue())) {
                    System.out.println(String.format("%s matches %s", s, p.getKey()));
                    matched = true;
                    break;
                }
            }
            if (! matched) {
                System.out.println(String.format("%s not matched", s));
            }
        }
        System.out.println();
    }

    public static List<Map.Entry<String, Pattern>> makePatterns (String syntax) {
        ResourceBundle resources = ResourceBundle.getBundle(syntax);
        List<Map.Entry<String, Pattern>> patterns = new ArrayList<>();
        Enumeration<String> iter = resources.getKeys();
        while (iter.hasMoreElements()) {
            String key = iter.nextElement();
            String regex = resources.getString(key);
            patterns.add(new AbstractMap.SimpleEntry<String, Pattern>(key,
                         // THIS IS THE KEY LINE
                         Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
        }
        return patterns;
    }


    public static void main (String[] args) {
        String[] examples = {
                "# foo",
                "foo #",
                "#",
                "",
                "fd",
                "FD",
                "forwardd",
                "allOrNothing",
                "all_or_nothing",
                "allOr_nothing?",
                "allOr?nothing_",
                ":allornothing",
                "90",
                "9.09",
                "9.0.0",
                "[",
                "]",
                "(",
                ")"
        };
        testMatches(examples, makePatterns("resources/languages/Syntax"));
    }
}
