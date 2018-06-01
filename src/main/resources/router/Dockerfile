FROM phusion/baseimage:0.10.1

RUN echo "deb http://archive.ubuntu.com/ubuntu trusty main universe" > /etc/apt/sources.list

RUN apt-get -y update

RUN DEBIAN_FRONTEND=noninteractive apt-get install -y -q python-software-properties software-properties-common

RUN apt-get update \
    && apt-get install -y traceroute iptables curl dnsutils \
                          net-tools \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /
CMD ["/sbin/my_init"]
