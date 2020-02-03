package common.util;

import lombok.SneakyThrows;

import java.io.*;
import static common.util.TestBase.*;

public class FileUtils {

    private static final String ENV_PROP_FILE = "environment.properties";
    private static final String DOWNLOAD_FILE_TO_FOLDER = "\\src\\test\\resources\\tmp\\";
    public static final String downloadFilePath = getPathToUploadFile() + DOWNLOAD_FILE_TO_FOLDER;
    private static final String PATH_TO_FILE = "src/main/resources/";

    @SneakyThrows
    public static void createDirectoryForDownloadingFiles() {
        File rootDir = new File(downloadFilePath);
        if (!rootDir.exists()) {
            log.info("Creating directory: " + rootDir.getName());
            rootDir.mkdir();
            log.info("DIRECTORY has been created");
        }
    }
    @SneakyThrows
    public static void createDirectoryForDownloadingFiles(String tempFolderName) {
        String tempFolder = downloadFilePath + "\\" + tempFolderName;
        File rootDir = new File(downloadFilePath);
        File tempDir = new File(tempFolder);
        if (!rootDir.exists()) {
            log.info("Creating ROOT directory: " + rootDir.getName());
            rootDir.mkdir();
            log.info("Created directory: " + rootDir.getName());
        }
        if (!tempDir.exists()) {
            log.info("Creating TEMP directory: " + tempDir.getName());
            tempDir.mkdir();
            log.info("Created directory: " + tempDir.getName());
        }
    }
    public static String getPathToUploadFile() {
        return new File("").getAbsolutePath();
    }


}
