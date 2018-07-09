package com.redhat.patriot.network_simulator.example.image;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.redhat.patriot.network_simulator.example.files.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class DockerImage  implements Image{
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImage.class);

    private DockerClient dockerClient;

    public DockerImage(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public void buildImage(Set<String> tag, String path) {

        FileUtils fileUtils = new FileUtils();
        File dockerFile = new File(DockerImage.class.getClassLoader().getResource(path).getFile());
        if (dockerFile.exists()) {

            LOGGER.info("Looking in resources");
            dockerClient.buildImageCmd(dockerFile)
                    .withTags(tag).exec(new BuildImageResultCallback()).awaitImageId();

        } else if (DockerImage.class.getClassLoader().getResourceAsStream(path) != null){

            LOGGER.info("Looking in root");

            try {
                Path tmpDir = Files.createTempDirectory(Paths.get(""),"tmpDir");
                File docker = new File(fileUtils.convertToFile(DockerImage.class.getClassLoader()
                        .getResourceAsStream(path), tmpDir.toAbsolutePath() + "/Dockerfile"));

                File script = new File(fileUtils.convertToFile(DockerImage.class.getClassLoader()
                        .getResourceAsStream("app/setGW"), tmpDir.toAbsolutePath() + "/setGW"));

                script.setExecutable(true);

                dockerClient.buildImageCmd(docker).withTags(tag).exec(new BuildImageResultCallback()).awaitImageId();

                fileUtils.deleteDirWithFiles(tmpDir.toFile());

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            LOGGER.warn("DOCKERFILES does not exists");
            System.exit(0);
        }
    }

    @Override
    public void deleteImage(Set<String> tags) {

    }

    public void deleteImage(String imageTag) {
        dockerClient.removeImageCmd(imageTag).exec();
    }



}
