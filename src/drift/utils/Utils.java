package drift.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Utils {
    public static String loadFileAsString(String path) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

}
