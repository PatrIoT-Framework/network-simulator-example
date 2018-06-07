package com.redhat.patriot.network_simulator.example.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.container.DockerContainer;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.ArrayList;
import java.util.List;

public class DockerManagerImpl implements Manager {

    private DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();
    @Override
    public Container createContainerResponse() {
        return null;
    }

    @Override
    public Network createNetwork() {
        return null;
    }

    @Override
    public List<Container> listContainers() {
        List<com.github.dockerjava.api.model.Container> outputConts = dockerClient.listContainersCmd()
                .withShowAll(true).exec();
        List<Container> dockerContainers = new ArrayList<>();
        for (com.github.dockerjava.api.model.Container c: outputConts) {
            dockerContainers.add(new DockerContainer(c.getNames(), c.getId()));
        }
        return dockerContainers;
    }
}
