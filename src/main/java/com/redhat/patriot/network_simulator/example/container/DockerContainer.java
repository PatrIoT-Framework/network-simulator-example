package com.redhat.patriot.network_simulator.example.container;

import com.redhat.patriot.network_simulator.example.manager.DockerManager;
import com.redhat.patriot.network_simulator.example.manager.Manager;
import com.redhat.patriot.network_simulator.example.network.Network;

import java.util.List;
import java.util.stream.Collectors;

public class DockerContainer implements Container {
    String name;
    String id;
    DockerManager dockerManager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DockerContainer(String name, String id, DockerManager dockerManager) {
        this.name = name;
        this.id = id;
        this.dockerManager = dockerManager;
    }

    public DockerContainer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Manager getManager() {
        return dockerManager;
    }

    public void setManager(Manager manager) {
        this.dockerManager = (DockerManager) manager;
    }

    @Override
    public boolean isAlive() {
        List<Container> aliveCont = dockerManager.listContainers().stream()
                .filter(DockerContainer -> DockerContainer.getId().equals(this.id))
                .collect(Collectors.toList());

        if (aliveCont.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean exist() {
        return false;
    }

    @Override
    public Network connectToNetwork(Network network) {
        return null;
    }

    @Override
    public void destroyContainer() {

    }

}
