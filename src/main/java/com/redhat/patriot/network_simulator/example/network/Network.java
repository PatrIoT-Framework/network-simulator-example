package com.redhat.patriot.network_simulator.example.network;

import com.redhat.patriot.network_simulator.example.manager.Manager;

public interface Network {
    void exists(Manager dockerManager);
    void destroyNetwork(Manager dockerManager);
}
