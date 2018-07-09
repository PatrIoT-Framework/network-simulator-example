package com.redhat.patriot.network_simulator.example.image;

import java.util.Set;

public interface Image {
    void buildImage(Set<String> tags, String imageSource);
    void deleteImage(Set<String> tags);
}
