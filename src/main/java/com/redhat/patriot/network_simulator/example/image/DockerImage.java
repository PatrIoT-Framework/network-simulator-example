package com.redhat.patriot.network_simulator.example.image;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.redhat.patriot.network_simulator.example.files.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DockerImage {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImage.class);

    private DockerClient dockerClient;

    public DockerImage(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public void buildImage(String tag, String path) {

        FileUtils fileUtils = new FileUtils();

        if (new File(DockerImage.class.getClassLoader().getResource(path).getFile()).exists()) {

            LOGGER.info("Looking in resources");
            dockerClient.buildImageCmd(new File(DockerImage.class.getClassLoader().getResource(path).getFile()))
                    .withTag(tag).exec(new BuildImageResultCallback()).awaitImageId();

        } else if (DockerImage.class.getClassLoader().getResourceAsStream(path) != null){

            LOGGER.info("Looking in root");

            File tempDir = new File("tmpDir");
            tempDir.mkdir();

            File docker = new File(fileUtils.convertToFile(DockerImage.class.getClassLoader()
                    .getResourceAsStream(path), tempDir.getAbsolutePath() + "/Dockerfile"));

            File script = new File(fileUtils.convertToFile(DockerImage.class.getClassLoader()
                    .getResourceAsStream("app/setGW"), tempDir.getAbsolutePath() + "/setGW"));

            script.setExecutable(true);

            dockerClient.buildImageCmd(docker).withTag(tag).exec(new BuildImageResultCallback()).awaitImageId();

            fileUtils.deleteDirWithFiles(tempDir);

        } else {
            LOGGER.warn("DOCKERFILES does not exists");
            System.exit(0);
        }
    }



}
