FROM openjdk:8

MAINTAINER Carey Janecka <careyjanecka@gmail.com>

COPY . /tmp/

EXPOSE 80

CMD ["/tmp/gradlew", "-p/tmp", "run"]