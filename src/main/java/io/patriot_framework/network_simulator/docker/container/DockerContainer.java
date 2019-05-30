/*
 * Copyright 2019 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.network_simulator.docker.container;

import io.patriot_framework.network.simulator.api.model.network.Network;
import io.patriot_framework.network_simulator.docker.manager.DockerManager;
import io.patriot_framework.network_simulator.docker.manager.Manager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Instance of DockerContainer representing informations which are required for work with container. Class is providing
 * basic work with container. This type of work is later executed in DockerManager.
 */
public class DockerContainer implements Container {
    private String name;
    private String id;
    private DockerManager dockerManager;

    public DockerContainer(String id) {
        this.id = id;
    }

    /**
     * Instantiates a new Docker container.
     *
     * @param name          the name
     * @param id            the id
     * @param dockerManager the docker manager
     */
    public DockerContainer(String name, String id, DockerManager dockerManager) {
        this.name = name;
        this.id = id;
        this.dockerManager = dockerManager;
    }

    /**
     * Instantiates a new Docker container.
     *
     * @param name the name
     * @param id   the id
     */
    public DockerContainer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIpAddress(Network network) {
        return dockerManager.findIpAddress(this, network);
    }

    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets manager.
     *
     * @return the manager
     */
    public Manager getManager() {
        return dockerManager;
    }

    /**
     * Sets manager.
     *
     * @param manager the manager
     */
    public void setManager(Manager manager) {
        this.dockerManager = (DockerManager) manager;
    }

    /**
     * Method returns if is container alive.
     * @return returns if container is alive
     */
    @Override
    public boolean exists() {
        List<Container> aliveCont = dockerManager.listContainers().stream()
                .filter(DockerContainer -> DockerContainer.getId().equals(this.id))
                .collect(Collectors.toList());

        return !aliveCont.isEmpty();
    }

    /**
     * Method provides connecting container to networks.
     *
     * @param networks networks to which will be container connected
     */
    @Override
    public void connectToNetwork(List<Network> networks) {
        for (Network network : networks) {
            dockerManager.connectContainerToNetwork(this, network);
        }
    }

    /**
     * Method provides soft delete of container (stop + delete).
     */
    @Override
    public void destroyContainer() {
        dockerManager.destroyContainer(this);
    }

    /**
     * Method gathers ip of container's gateway.
     *
     * @return String Ip address of container gateway
     */
    public String getGatewayNetworkIp() {
        return dockerManager.getDefaultGwNetworkIp(this);
    }

    /**
     * Method gathers CIDR mask of container's gateway.
     *
     * @return Integer CIDR mask of container gateway.
     */
    public Integer getGatewayNetworkMask() {
        return dockerManager.getDefaultGwNetworkMask(this);
    }

}
