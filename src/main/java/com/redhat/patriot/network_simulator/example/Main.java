package com.redhat.patriot.network_simulator.example;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.redhat.patriot.network_simulator.example.container.DockerCont;
import com.redhat.patriot.network_simulator.example.image.DockerImage;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        DockerClient dockerClient = DockerClientBuilder.
                getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();

        String tagApp = "app_test:01";
        String tagRouter  = "router_test:01";

        buildImages(tagApp, tagRouter, dockerClient);

        DockerNetwork dockerNetwork = new DockerNetwork();
        Network serverNetwork = dockerNetwork.createNetworkWithSubnet("172.22.0.0/16", "server_network", dockerClient);
        Network clientNetwork = dockerNetwork.createNetworkWithSubnet("172.23.0.0/16", "client_network", dockerClient );

        DockerCont dockerCont = new DockerCont(dockerClient);

        CreateContainerResponse router = dockerCont.createContainer(tagRouter, "router");
        dockerCont.connectContToNetwork(router, clientNetwork.getId());
        dockerCont.connectContToNetwork(router, serverNetwork.getId());
        dockerClient.startContainerCmd(router.getId()).exec();

        CreateContainerResponse client = dockerCont.createContainer(tagApp, "comm_client");
        CreateContainerResponse server = dockerCont.createContainer(tagApp, "comm_server");

        dockerCont.connectContToNetwork(client, clientNetwork.getId());
        dockerCont.connectContToNetwork(server, serverNetwork.getId());
        dockerClient.startContainerCmd(client.getId()).exec();
        dockerClient.startContainerCmd(server.getId()).exec();

        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd("router").exec();
        NetworkSettings serverResponse = dockerClient.inspectContainerCmd("comm_server").exec().getNetworkSettings();
        NetworkSettings containerNetworkSettings = containerResponse.getNetworkSettings();

        LOGGER.debug("Starting GW setup");

        dockerCont.runCommand(server, "./setGW " +
                containerNetworkSettings.getNetworks().get(serverNetwork.getName()).getIpAddress());
        dockerCont.runCommand(client,  "./setGW " +
                containerNetworkSettings.getNetworks().get(clientNetwork.getName()).getIpAddress());
    }

    public static void buildImages(String tagApp, String tagRouter, DockerClient dockerClient) {

        DockerImage dockerImage = new DockerImage(dockerClient);
        dockerImage.buildImage(tagApp, "app/Dockerfile");
        dockerImage.buildImage(tagRouter, "router/Dockerfile");
    }
}
