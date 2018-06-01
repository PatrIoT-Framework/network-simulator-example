package com.redhat.patriot.network_simulator.example.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;

import java.util.concurrent.TimeUnit;

public class DockerCont {
    private DockerClient dockerClient;

    public DockerCont(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public CreateContainerResponse createContainer(String tag, String name) {
        return dockerClient.createContainerCmd(tag)
                .withPrivileged(true)
                .withCmd()
                .withName(name)
                .exec();
    }
    public void connectContToNetwork(CreateContainerResponse cont, String networkId) {
        dockerClient.connectToNetworkCmd().withNetworkId(networkId).withContainerId(cont.getId()).exec();

    }

    public void runCommand(   final CreateContainerResponse container,
                               final String command) throws InterruptedException {

        String[] commandWithArguments = command.split("\\s+");
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withCmd(commandWithArguments)
                .withUser("root")
                .exec();
        dockerClient.execStartCmd(execCreateCmdResponse.getId()).
                exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion(10,TimeUnit.SECONDS);
    }


}
