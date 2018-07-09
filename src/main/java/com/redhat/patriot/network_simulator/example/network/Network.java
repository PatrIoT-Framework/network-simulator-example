package com.redhat.patriot.network_simulator.example.network;

import com.redhat.patriot.network_simulator.example.manager.Manager;

public interface Network {
    String getId();
    String getName();
    boolean exists(Manager dockerManager);
}
