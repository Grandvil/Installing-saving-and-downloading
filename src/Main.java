import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    private static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        taskOne();
        taskTwo();
        taskThree();
    }

    public static void creatingACatalog(File dir) {
        if (dir.mkdir())
            sb.append("Каталог " + dir + " создан.\n");
    }

    public static void creatingAFile(File myFile) throws IOException {
        try {
            if (myFile.createNewFile())
                sb.append("Файл " + myFile + " был создан.\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void logs(StringBuilder s, File f) {
        try (FileWriter writer = new FileWriter(f)) {
            writer.write(s.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void saveGame(String a, GameProgress b) {
        try (FileOutputStream fos = new FileOutputStream(a);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(b);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String a, List<String> b) {
        try (ZipOutputStream zout = new ZipOutputStream(new
                FileOutputStream(a))) {
            for (String srcFile : b) {
                File fileToZip = new File(srcFile);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry entry = new ZipEntry(fileToZip.getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void fileDelete(List<String> a) {
        for (String file : a) {
            File game = new File(file);
            if (!game.delete()) {
                game.deleteOnExit();
            }
        }
    }

    public static void openZip(String a, String b) {
        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(a))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
                Files.move(Paths.get(name), Paths.get(b + File.separator + entry.getName()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openProgress(String a) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(a);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(gameProgress);
    }

    public static void taskOne() throws IOException {
        String dirGames = "src/Games";

        File dirSrc = new File(dirGames + "/src");
        creatingACatalog(dirSrc);

        File dirRes = new File(dirGames + "/res");
        creatingACatalog(dirRes);

        File dirSaveGames = new File(dirGames + "/savegames");
        creatingACatalog(dirSaveGames);

        File dirTemp = new File(dirGames + "/temp");
        creatingACatalog(dirTemp);

        File dirMain = new File(dirGames + "/src/main");
        creatingACatalog(dirMain);

        File dirTest = new File(dirGames + "/src/test");
        creatingACatalog(dirTest);

        File fileMain = new File(dirMain, "Main.java");
        creatingAFile(fileMain);

        File fileUtils = new File(dirMain, "Utils.java");
        creatingAFile(fileUtils);

        File dirDrawables = new File(dirRes + "/drawables");
        creatingACatalog(dirDrawables);

        File dirVectors = new File(dirRes + "/vectors");
        creatingACatalog(dirVectors);

        File dirIcons = new File(dirRes + "/icons");
        creatingACatalog(dirIcons);

        File fileTemp = new File(dirTemp, "temp.txt");
        creatingAFile(fileTemp);

        logs(sb, fileTemp);
    }

    public static void taskTwo() {
        GameProgress levelOne = new GameProgress(97, 56, 1, 432.3);
        GameProgress levelTwo = new GameProgress(100, 55, 2, 1432.3);
        GameProgress levelThree = new GameProgress(56, 54, 3, 2432.3);

        String saveOne = "src/Games/savegames/save1.dat";
        saveGame(saveOne, levelOne);

        String saveTwo = "src/Games/savegames/save2.dat";
        saveGame(saveTwo, levelTwo);

        String saveThree = "src/Games/savegames/save3.dat";
        saveGame(saveThree, levelThree);

        String zip = "src/Games/savegames/zip.zip";
        List<String> listLevels = Arrays.asList(saveOne, saveTwo, saveThree);
        zipFiles(zip, listLevels);

        fileDelete(listLevels);
    }

    public static void taskThree() {
        String zip = "src/Games/savegames/zip.zip";
        String files = "src/Games/savegames2";
        String saveTwo = "src/Games/savegames2/save2.dat";
        openZip(zip, files);
        openProgress(saveTwo);
    }
}
