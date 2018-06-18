package com.redhat.patriot.network_simulator.example.network;

import com.github.dockerjava.api.model.Network;
import com.redhat.patriot.network_simulator.example.TestClass;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockerNetworkTest extends TestClass {

    @Test
    void createNetworkWithSubnet() {
        DockerManager dockerManager = new DockerManager();
        DockerNetwork dockerNetwork = (DockerNetwork) dockerManager.createNetwork("test_network", "175.16.0.0/16");


        List<Network> networks = dockerClient.listNetworksCmd().withNameFilter(dockerNetwork.getName()).exec();

        assertEquals(false, networks.isEmpty());

        dockerManager.destroyNetwork(dockerNetwork);

    }
}