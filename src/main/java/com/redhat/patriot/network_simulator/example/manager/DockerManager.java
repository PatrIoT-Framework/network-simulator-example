package com.redhat.patriot.network_simulator.example.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.container.DockerContainer;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DockerManager implements Manager {

    private DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();

    @Override
    public Container createContainerResponse(String name, String tag) {
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd(tag)
                .withPrivileged(true)
                .withCmd()
                .withName(name)
                .exec();
        return new DockerContainer(name, containerResponse.getId());
    }
    @Override
    public Network createNetwork(String name, String subnet) {
        com.github.dockerjava.api.model.Network.Ipam ipam = new com.github.dockerjava.api.model.Network.Ipam().withConfig(new com.github.dockerjava.api.model.Network.Ipam.Config().withSubnet(subnet));

        CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd().withName(name)
                .withDriver("bridge")
                .withIpam(ipam)
                .exec();

        return new DockerNetwork(name, networkResponse.getId());
    }
    @Override
    public List<Container> listContainers() {
        List<com.github.dockerjava.api.model.Container> outputConts = dockerClient.listContainersCmd()
                .withShowAll(true).exec();
        List<Container> dockerContainers = new ArrayList<>();
        for (com.github.dockerjava.api.model.Container c: outputConts) {
            dockerContainers.add(new DockerContainer(Arrays.toString(c.getNames()), c.getId()));
        }
        return dockerContainers;
    }
}
