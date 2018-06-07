package com.redhat.patriot.network_simulator.example.manager;

import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;

public interface Manager {
    Container createContainerResponse();
    Network createNetwork();
    List<Container> listContainers();

}
