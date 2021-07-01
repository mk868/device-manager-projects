FROM openjdk:16.0.1-jdk-buster

RUN apt-get update \
    && apt-get install -y fakeroot \
    && apt-get clean
