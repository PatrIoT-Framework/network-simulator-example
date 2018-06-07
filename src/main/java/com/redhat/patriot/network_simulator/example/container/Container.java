package com.redhat.patriot.network_simulator.example.container;

import com.redhat.patriot.network_simulator.example.manager.Manager;
import com.redhat.patriot.network_simulator.example.network.Network;

public interface Container {

    String getId();
    boolean isAlive();
    boolean exist();
    Network connectToNetwork(Network network);
    void destroyContainer();

}
