package com.redhat.patriot.network_simulator.example.files;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    public String convertToFile(InputStream inputStream, String name) {

        File targetFile = new File(name);
        try {
            java.nio.file.Files.copy(
                    inputStream,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(inputStream);
        return targetFile.getAbsolutePath();
    }

    public void deleteDirWithFiles(File dir) {
        String[]entries = dir.list();
        for(String s: entries){
            File currentFile = new File(dir.getPath(),s);
            currentFile.delete();
        }
        dir.delete();
    }

   /* public File findDockerResourceFile(String path) {

        File dockerFile = new File(DockerImage.class.getClassLoader().getResource(path).getFile());
        if (dockerFile.exists()) {

            LOGGER.info("Looking in resources");
            return dockerFile;

        } else if (FileUtils.class.getClassLoader().getResourceAsStream(path) != null){

            LOGGER.info("Looking in root");

            try {
                Path tmpDir = Files.createTempDirectory(Paths.get(""),"tmpDir");
                File docker = new File(convertToFile(DockerImage.class.getClassLoader()
                        .getResourceAsStream(path), tmpDir.toAbsolutePath() + "/Dockerfile"));

                File script = new File(convertToFile(DockerImage.class.getClassLoader()
                        .getResourceAsStream("app/setGW"), tmpDir.toAbsolutePath() + "/setGW"));

                deleteDirWithFiles(tmpDir.toFile());

            } catch (IOException e) {
                e.printStackTrace();
            }
    }*/
}

