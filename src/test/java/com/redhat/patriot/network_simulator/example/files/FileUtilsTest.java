package com.redhat.patriot.network_simulator.example.files;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilsTest {

    @Test
    void convertToFile() {

        try {
            File tmpDir = new File("tmpTestDir");
            tmpDir.mkdir();
            FileUtils fileUtils = new FileUtils();
            File testFile = new File(tmpDir.getAbsolutePath() + "/testFile");
            testFile.createNewFile();
            writeToFile(testFile.getAbsolutePath());
            File testedFile = new File(fileUtils.convertToFile(new FileInputStream(testFile),tmpDir.getAbsolutePath() + "/testedFile"));

            assertEquals(true, org.apache.commons.io.FileUtils.contentEquals(testedFile,testFile));

            fileUtils.deleteDirWithFiles(tmpDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    void deleteDirWithFiles() {
        try {
            File tempDir = new File("tmpDir");
            tempDir.mkdir();
            File testFile = new File(tempDir.getAbsolutePath() + "/testFile");
            testFile.createNewFile();

            FileUtils fileUtils = new FileUtils();
            fileUtils.deleteDirWithFiles(tempDir);
            assertEquals(false, tempDir.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void writeToFile(String path) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(path, "UTF-8");
            for (int i = 0; i < 50; i++) {
                writer.println(i);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}