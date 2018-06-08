package com.redhat.patriot.network_simulator.example.manager;

import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;
import java.util.Set;

public interface Manager {
    Container createContainerResponse(String name, String tag);
    Network createNetwork(String name, String subnet);
    List<Container> listContainers();

}
