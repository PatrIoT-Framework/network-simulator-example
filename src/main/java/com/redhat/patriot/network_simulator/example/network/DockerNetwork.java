package com.redhat.patriot.network_simulator.example.network;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.redhat.patriot.network_simulator.example.manager.Manager;

public class DockerNetwork implements com.redhat.patriot.network_simulator.example.network.Network {
    private DockerClient dockerClient;
    private String name;
    private String id;

    public DockerNetwork(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public DockerNetwork(DockerClient dockerClient, String name, String id) {

        this.dockerClient = dockerClient;
        this.name = name;
        this.id = id;
    }

    /*public DockerNetwork(DockerClient dockerClient) {
            this.dockerClient = dockerClient;
        }

        public Network createNetworkWithSubnet(String subnet, String name) {
            Network.Ipam ipam = new Network.Ipam().withConfig(new Network.Ipam.Config().withSubnet(subnet));

            CreateNetworkResponse networkResponse = dockerClient.createNetworkCmd().withName(name)
                    .withDriver("bridge")
                    .withIpam(ipam)
                    .exec();
            Network network = dockerClient.inspectNetworkCmd().withNetworkId(networkResponse.getId()).exec();
            return network;
        }

        public void deleteNetwork(String name) {
            dockerClient.removeNetworkCmd(name).exec();
        }
    */
    @Override
    public void exists(Manager dockerManager) {

    }


    @Override
    public void destroyNetwork(Manager dockerManager) {

    }
}
