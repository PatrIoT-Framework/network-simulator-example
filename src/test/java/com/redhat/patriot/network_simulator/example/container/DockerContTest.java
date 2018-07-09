package com.redhat.patriot.network_simulator.example.container;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Network;
import com.redhat.patriot.network_simulator.example.TestClass;
import com.redhat.patriot.network_simulator.example.image.DockerImage;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockerContTest extends TestClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContTest.class);
    @Test
    void createContainer() {

        DockerManager dockerManager = new DockerManager();

        DockerImage dockerImage = new DockerImage(dockerClient);

        List<String> tags = Arrays.asList("testtag:02");
        List<String> nameOfCont = Arrays.asList("test_cont");

        dockerImage.buildImage(new HashSet<>(tags), "router/Dockerfile");
        DockerContainer dockerCont = (DockerContainer) dockerManager.createContainer("test_cont", "testtag:02");

        List<Container> outputConts = dockerClient.listContainersCmd().withShowAll(true)
                .withNameFilter(nameOfCont).exec();

        assertEquals(false, outputConts.isEmpty());
        dockerManager.destroyContainer(dockerCont);
    }

    @Test
    void connectContToNetwork() {

        List<String> tags = Arrays.asList("test_tag:01");
        DockerManager dockerManager = new DockerManager();
        DockerImage dockerImage = new DockerImage(dockerClient);
        dockerImage.buildImage(new HashSet<>(tags), "router/Dockerfile");

        DockerContainer dockerCont = (DockerContainer) dockerManager
                .createContainer("test_cont", tags.get(0));

        DockerNetwork dockerNetwork = (DockerNetwork) dockerManager
                .createNetwork("test_network", "172.42.0.0/16");


        dockerCont.connectToNetwork(Arrays.asList(dockerNetwork));

        Assertions.assertNotNull(dockerClient.inspectContainerCmd(dockerCont.getId()).exec()
                .getNetworkSettings().getNetworks().toString().contains(dockerNetwork.getId()));

        dockerManager.destroyContainer(dockerCont);
        dockerManager.destroyNetwork(dockerNetwork);
        dockerImage.deleteImage(tags.get(0));
    }
}