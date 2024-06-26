# Microservice with Docker
- One of the advantages of microservices is that each microservice is independent form another. Each can have a different programming language and database system
- Problem: having different tech/programming language in every microservice cause deployments to be complex. You don't want different deployment procedures for each different microservice types.

## Docker Image
- ***Docker image***:
    - Contains everything a microservice needs to run: application runtime (JDK, Python, NodeJS etc), application code, dependencies
    - A static template, set of bytes
    - When an image is run it is called ***Docker container***
    - You can run multiple container from same image
- With a docker image, you can run it as ***Docker containers*** the same way on any infrastucture(local machine, AWS, Azure...)
- Docker images are built on top of ***DockerEngine***. DockerEngine is built on top of current operating system

## Deployment
- Traditional/old way of deployment:
    1. choose hardware based on requirements
    2. Install specific OS and version of it
    3. Install application runtime dependencies (ex: Java 17, tomcat etc)
    4. Install the application you want to deploy
- Traditional way of deployment is too manual and prone to error when deploying
- With Docker you can deploy your application with simple Docker instructions

## Why Docker is Popular
- Before docker virtual machines were popular:
    - Hypervisor: manages virtual machines
    - Each virtual machine had its own guest OS
    - On top of each guest OS, there were softwares needed to run application. And on top of it you application would lie.
- Major problem with VM: typically they were heavy weight since there is one host OS and guest OS for each VM
- With Docker, all you need to do is install docker engine on top of specific host OS. Docker engine take care of managing all containers. There is only 1 OS: host; thus docker is very efficient and lightweight
![docker.jpg](./docker.jpg)

## Architecture of Docker
- ***Docker client***: place where we are running docker commands
- ***Docker Daemon/engine***: 
    - docker commands are sent here for execution
    - manages containers, local images, pulling/pushing from/to image registry
    - can be used to create images as well
![docker-arc.jpg](docker-arc.png)

## Analyzing Example From in28min
- Docker command used to deploy spring boot application: `docker run in28min/todo-rest-api-h2:1.0.0.RELEASE`
- When this command is run, the image is downloaded from `hub.docker.com` ***(docker registry)***
- ***Docker registry***: contains repos and apps with different versions
- ***Docker repository***: 
    - `hub.docker.com/r/in28min/todo-rest-api-h2`
    - Stores all the versions of a specific app

### Bridge Network
- Notice that when the docker container is running in your machine you won't be able to access `localhost:5000/hello-world`
- Correct command: 
    - `docker run -p 5000:5000 in28min/todo-rest-api-h2:1.0.0.RELEASE`
    - `-p {HostPort}:{ContainerPort}`
- By default any container you run is part of ***bridge network*** in Docker. If you want to access to the container, you have to expose container port to the host port.
- In our example the spring boot application is set to run at port 5000. This port will be used inside docker network. If we wish we can access to our application from any port we want from our machine `docker run -p 1234:5000 in28min/todo-rest-api-h2:1.0.0.RELEASE` --> localhost:1234/hello-world

## Options in Docker
- `-d`: detached mode, this detaches docker process from terminal. Docker will run in the background even if you press ctrl-c in terminal
- `-f`: tails the logs of app

## Playing with Images
- You can give multiple tags to a single image:
```bash
docker run in28min/todo-rest-api-h2:1.0.0.RELEASE #create image with tag 1.0.0.RELEASE

docker tag in28min/todo-rest-api-h2:1.0.0.RELEASE in28min/todo-rest-api-h2:latest #create image with tag latest from existing image

docker images # you can see that latest and 1.0.0.RELEASE both point to the same version
REPOSITORY                 TAG             IMAGE ID       CREATED         SIZE
in28min/todo-rest-api-h2   1.0.0.RELEASE   814e9d2807cc   22 months ago   141MB
in28min/todo-rest-api-h2   latest          814e9d2807cc   22 months ago   141MB
```
- `latest` tag doesn't mean anything in docker, it is just a name. When you execute `docker pull mysql:latest`, you are not guaranteed to pull the latest version
- `docker pull ...`: pulls an image but doesn't run
- `docker run ...`: pulls an image if not available in local, then runs it
- Searching for images and identifying official images:
```bash
docker search mysql

NAME                            DESCRIPTION                                     STARS     OFFICIAL
mysql                           MySQL is a widely used, open-source relation…   15006     [OK]
circleci/mysql                  MySQL is a widely used, open-source relation…   29        
```
- Official images are maintained by DockerHub, so you would want to use official images for your apps whenever possible
- `docker image history 814e9d2807cc`: lists steps involved in creating a specific image
- `docker image inspect 814e9d2807cc`: you can find all the meta data for the image
- `docker image remove 814e9d2807cc`: deletes image from local

## Playing with Containers
- `docker container pause b828e61...`: To pause/unpause a container (use container id)
- `docker container inspect`
- `docker container prune`: removes all stopped containers
- `docker stop b828e61...`: 
    - ***gracefully*** shutdowns the container
    - This command gives container few seconds to finish it processes: shuts down ExecutorService, closes entity manager factory, drops tables/sequences, shuts down connection pool. This is called ***SIGTERM*** 
- `docker kill b828e61...`: stop the container as it is, immediately. This is called ***SIGKILL***
- `docker run --restart=always in28min/todo...`: 
    - automatically starts the container whenever you open Docker Desktop. 
    - Default value is `no`
    - Very useful for the images that you want them to be always running, such as DB

## Playing with Commands
- `docker container ls`: lists all containers
- `docker events`: logs all the events that are happening in background in docker: which container is paused, stopped, started etc
- `docker top b828e61...`: sows the top process which is running in a container
- `docker stats`: shows stats for all running containers
- `docker run -m 512m --cpu-quota 5000 in28min/todo...`:
    - assign a limit for memory usage of 512 megabyte (you can use *G* for gigabyte)
    - assign a limit for CPU usage of 5000 (max CPU usage is 100000)
- `docker system df`: show everything that docker daemon manages