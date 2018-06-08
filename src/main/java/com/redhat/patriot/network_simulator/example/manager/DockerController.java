package com.redhat.patriot.network_simulator.example.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkSettings;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.redhat.patriot.network_simulator.example.cleanup.Cleaner;
import com.redhat.patriot.network_simulator.example.container.DockerCont;
import com.redhat.patriot.network_simulator.example.image.DockerImage;
import com.redhat.patriot.network_simulator.example.network.DockerNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DockerController {
    private DockerClient dockerClient = DockerClientBuilder.
            getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder().build()).build();
    private DockerCont dockerCont = new DockerCont(dockerClient);


    public void genererateEnviroment() {
        List<String> networks = new ArrayList<>();
        List<String> conts = new ArrayList<>();
        try {
            String tagApp = "app_test:01";
            String tagRouter  = "router_test:01";

            buildImages(tagApp, tagRouter, dockerClient);

            DockerNetwork dockerNetwork = new DockerNetwork(dockerClient);
            Network serverNetwork = dockerNetwork.createNetworkWithSubnet("172.22.0.0/16",
                    "server_network");
            networks.add(serverNetwork.getName());
            Network clientNetwork = dockerNetwork.createNetworkWithSubnet("172.23.0.0/16",
                    "client_network");
            networks.add(clientNetwork.getName());

            CreateContainerResponse router = createConnectAndStart(tagRouter ,"router",
                    Arrays.asList(clientNetwork.getId(), serverNetwork.getId()));
            conts.add("router");
            CreateContainerResponse client = createConnectAndStart(tagApp , "comm_client",
                    Arrays.asList(clientNetwork.getId()) );
            conts.add("comm_client");

            CreateContainerResponse server = createConnectAndStart(tagApp ,"comm_server",
                    Arrays.asList(serverNetwork.getId()));
            conts.add("comm_server");

            setGW(client, server, networks);

        } catch (Exception e ) {
            Cleaner cleaner = new Cleaner(dockerClient);
            cleaner.cleanUp(networks, conts);
            e.printStackTrace();
        }

    }

    void buildImages(String tagApp, String tagRouter, DockerClient dockerClient) {

        DockerImage dockerImage = new DockerImage(dockerClient);
        dockerImage.buildImage(new HashSet<>(Arrays.asList(tagApp)), "app/Dockerfile");
        dockerImage.buildImage(new HashSet<>(Arrays.asList(tagRouter)), "router/Dockerfile");
    }

    CreateContainerResponse createConnectAndStart(String tag, String name, List<String> networkIds) {

        CreateContainerResponse container = dockerCont.createContainer(tag, name);
        for (String networkId: networkIds) {
            dockerCont.connectContToNetwork(container, networkId);
        }
        dockerClient.startContainerCmd(container.getId()).exec();

        return container;
    }

    void setGW(CreateContainerResponse client, CreateContainerResponse server,
               List<String> networks) throws InterruptedException {

        InspectContainerResponse containerResponse = dockerClient.inspectContainerCmd("router").exec();
        NetworkSettings netSettings = containerResponse.getNetworkSettings();
        dockerCont.runCommand(server, "./setGW " +
                netSettings.getNetworks().get(networks.get(0)).getIpAddress());
        dockerCont.runCommand(client,  "./setGW " +
                netSettings.getNetworks().get(networks.get(1)).getIpAddress());
    }
}
