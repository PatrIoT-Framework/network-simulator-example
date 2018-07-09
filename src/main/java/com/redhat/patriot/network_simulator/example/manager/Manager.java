package com.redhat.patriot.network_simulator.example.manager;

import com.redhat.patriot.network_simulator.example.container.Container;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;

public interface Manager {
    Container createContainer(String name, String tag);
    Network createNetwork(String name, String subnet);
    List<Container> listContainers();
    List<Network> listNetworks();
    void connectContainerToNetwork(Container container,Network network);
    void destroyContainer(Container container);
    void destroyNetwork(Network network);
    void runCommand(Container container, String command);
    void startContainer(Container container);

}
