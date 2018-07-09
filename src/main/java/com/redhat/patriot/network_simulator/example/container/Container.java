package com.redhat.patriot.network_simulator.example.container;

import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;

public interface Container {
    String getName();
    String getId();
    boolean isAlive();
    boolean exists();
    void connectToNetwork(List<Network> networks);
    void destroyContainer();

}
