package yuelengm.exmachina.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ScriptFileUtil {
    private static String folderPath = "./em-script";

    public static void init() {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
    }

    public static List<String> listNames() {
        List<String> names = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        for (File f : files) {
            names.add(f.getName());
        }

        return names;
    }

    public static List<String> getScript(String fileName) {
        try {
            return Files.readAllLines(Paths.get(folderPath + "/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
