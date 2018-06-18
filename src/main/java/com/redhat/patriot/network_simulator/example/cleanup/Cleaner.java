package com.redhat.patriot.network_simulator.example.cleanup;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Cleaner {
    private DockerClient dockerClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(Cleaner.class);
    public Cleaner(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public void cleanUp(List<String> networks, List<String> containers) {

        List<Container> outputCont = dockerClient.listContainersCmd().withShowAll(true).
                withNameFilter(containers).exec();

        if (!outputCont.isEmpty()) {
            for (Container container: outputCont) {

                if (!container.getStatus().contains("Exited") && !container.getStatus().contains("Created")) {
                    dockerClient.killContainerCmd(container.getId()).exec();
                }
                dockerClient.removeContainerCmd(container.getId()).exec();
            }
        }


        LOGGER.info("Trying to clear networks");
        if (!networks.isEmpty()) {
            for (String networkName : networks) {
                List<Network> network = dockerClient.listNetworksCmd().withNameFilter(networkName).exec();
                LOGGER.info("Network " + network.get(0).getName() + " is being cleared");
                dockerClient.removeNetworkCmd(network.get(0).getName()).exec();
            }
        }
    }
}
