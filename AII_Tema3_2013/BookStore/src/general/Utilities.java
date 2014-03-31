package general;

import java.util.Random;
import java.util.StringTokenizer;

public class Utilities {
    public static String removeSpaces(String text) {
        String result = new String();
        StringTokenizer stringTokenizer = new StringTokenizer(text," ");
        while (stringTokenizer.hasMoreTokens())
            result += stringTokenizer.nextToken();
        return result;
    }
    
    public static String generateBillNumber() {
        String result = new String();
        Random random = new Random();
        for (int currentIndex = 0; currentIndex < 3; currentIndex++)
            result += (char)('A'+random.nextInt(26));
        result += random.nextInt(1000000);
        return result;
    }
    
    public static boolean isSystemFunction(String record) {
        for (String function:Constants.SYSTEM_FUNCTION)
            if (record.contentEquals(function))
                return true;
        return false;
    }
}
