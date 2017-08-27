FROM openjdk:8

MAINTAINER Carey Janecka <careyjanecka@gmail.com>

COPY . /tmp/

CMD ["/tmp/gradlew", "-p/tmp", "run"]