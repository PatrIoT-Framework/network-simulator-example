package com.redhat.patriot.network_simulator.example.cleanup;

import com.redhat.patriot.network_simulator.example.TestClass;
import org.junit.jupiter.api.Test;

class CleanerTest extends TestClass {

    @Test
    void cleanUp() {

       /* Cleaner cleaner = new Cleaner(dockerClient);
        DockerCont dockerCont = new DockerCont(dockerClient);
        DockerNetwork dockerNetwork = new DockerNetwork(dockerClient);
        DockerImage dockerImage = new DockerImage(dockerClient);

        List<String> tags = Arrays.asList("test_tag:01");
        List<String> conts = Arrays.asList("test_cont01", "test_cont02");
        List<String> networks = Arrays.asList("test_network");

        dockerImage.buildImage(new HashSet<>(tags), "router/Dockerfile");

        for (String container:conts) {
            dockerCont.createContainer(tags.get(0), container);
        }

        dockerNetwork.createNetworkWithSubnet("172.42.0.0/16", networks.get(0));


        cleaner.cleanUp(networks, conts);

        List<Container> outputConts = dockerClient.listContainersCmd().withShowAll(true)
                .withNameFilter(conts).exec();
        List<Network> outputNetworks = dockerClient.listNetworksCmd().withNameFilter(networks.get(0)).exec();

        assertEquals(true, outputConts.isEmpty());
        assertEquals(true, outputNetworks.isEmpty());

        dockerImage.deleteImage(tags.get(0));*/

    }
}