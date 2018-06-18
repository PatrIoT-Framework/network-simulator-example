package com.redhat.patriot.network_simulator.example;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.redhat.patriot.network_simulator.example.cleanup.Cleaner;
import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.image.DockerImage;
import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DockerController {
    private DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();
    private DockerManager dockerManager = new DockerManager();
    private Logger LOGGER = LoggerFactory.getLogger(DockerController.class);

    public void genererateEnviroment() {
        List<String> networks = new ArrayList<>();
        List<String> conts = new ArrayList<>();
        try {
            DockerManager dockerManager = new DockerManager();
            String tagApp = "app_test:01";
            String tagRouter  = "router_test:01";

            buildImages(tagApp, tagRouter, dockerClient);

            DockerNetwork serverNetwork =
                    (DockerNetwork) dockerManager.createNetwork("server_network", "172.22.0.0/16");
            networks.add(serverNetwork.getName());
            DockerNetwork clientNetwork =
                    (DockerNetwork) dockerManager.createNetwork("client_network", "172.23.0.0/16");
            networks.add(clientNetwork.getName());

            Container router = dockerManager.createContainer("routerino",tagRouter);
            router.connectToNetwork(Arrays.asList(clientNetwork, serverNetwork));
            dockerManager.startContainer(router);
            conts.add(router.getName());

            Container commClient = dockerManager.createContainer("comm_client", tagApp);
            commClient.connectToNetwork(Arrays.asList(clientNetwork));
            dockerManager.startContainer(commClient);
            conts.add(commClient.getName());

            Container commServer = dockerManager.createContainer("comm_server", tagApp);
            commServer.connectToNetwork(Arrays.asList(serverNetwork));
            dockerManager.startContainer(commServer);
            conts.add(commServer.getName());

            setGW(commClient, commServer, networks, router);

        } catch (Exception e ) {
            e.printStackTrace();
            Cleaner cleaner = new Cleaner(dockerClient);
            cleaner.cleanUp(networks, conts);
        }

    }

    void buildImages(String tagApp, String tagRouter, DockerClient dockerClient) {
        DockerImage dockerImage = new DockerImage(dockerClient);
        dockerImage.buildImage(new HashSet<>(Arrays.asList(tagApp)), "app/Dockerfile");
        dockerImage.buildImage(new HashSet<>(Arrays.asList(tagRouter)), "router/Dockerfile");
    }


    void setGW(Container client, Container server, List<String> networks, Container router){

        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd(router.getName()).exec();
        NetworkSettings netSettings = containerResponse.getNetworkSettings();

        dockerManager.runCommand(server, "./setGW " +
                netSettings.getNetworks().get(networks.get(0)).getIpAddress());

        dockerManager.runCommand(client, "./setGW " +
                netSettings.getNetworks().get(networks.get(1)).getIpAddress());
    }
}
