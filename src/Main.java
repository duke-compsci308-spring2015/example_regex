import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Main {  
    public static boolean match (String input, Pattern regex) {
        // THIS IS THE KEY LINE
        return regex.matcher(input).matches();
        // basic strings can match also, but not using a Pattern
        // return input.matches(regex);
    }

    private static void testMatches (String[] tests, List<Entry<String, Pattern>> patterns) {
        for (String s : tests) {
            boolean matched = false;
            if (s.trim().length() > 0) {
                for (Entry<String, Pattern> p : patterns) {
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
        }
        System.out.println();
    }

    public static List<Entry<String, Pattern>> makePatterns (String syntax) {
        ResourceBundle resources = ResourceBundle.getBundle(syntax);
        List<Entry<String, Pattern>> patterns = new ArrayList<>();
        Enumeration<String> iter = resources.getKeys();
        while (iter.hasMoreElements()) {
            String key = iter.nextElement();
            String regex = resources.getString(key);
            patterns.add(new SimpleEntry<String, Pattern>(key,
                         // THIS IS THE KEY LINE
                         Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
        }
        return patterns;
    }


    // BUGBUG: very bad practice to throw from main :(
    public static void main (String[] args) throws FileNotFoundException {
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
        String userInput = "fd 50 rt 90 BACK :distance Left :angle";
        String fileInput = new Scanner(new File("square.logo")).useDelimiter("\\Z").next();

        // create a list of things to check
        List<Entry<String, Pattern>> patterns = new ArrayList<>();
        // these are more specific, so add them first to ensure they are checked first
        patterns.addAll(makePatterns("resources/languages/English"));
        patterns.addAll(makePatterns("resources/languages/Syntax"));

        // try against different inputs
        testMatches(examples, patterns);
        testMatches(userInput.split("\\p{Space}"), patterns);
        testMatches(fileInput.split("\\p{Space}"), patterns);
    }
}
