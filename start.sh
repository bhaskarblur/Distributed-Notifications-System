#!/bin/bash

# Define the base directory for the services
BASE_DIR="./"  # Change this to the path where your Dockerfiles are located
DOCKER_HUB_USERNAME="bhaskarblur"  # Change this to your Docker Hub username or your local Docker registry prefix
DOCKER_COMPOSE_FILE="$BASE_DIR/docker-compose-swarm.yml"

# Array of service directories
declare -a services=("user-service" "order-service" "notification-service")

# Function to build and push Docker images
build_and_push() {
    for service in "${services[@]}"; do
        echo "Building image for $service..."
        docker build -t "$DOCKER_HUB_USERNAME/$service:latest" "$BASE_DIR/$service" || exit 1
        echo "Pushing $DOCKER_HUB_USERNAME/$service:latest to Docker Hub..."
        docker push "$DOCKER_HUB_USERNAME/$service:latest" || exit 1
    done
}

# Function to deploy using Docker Compose
deploy() {
    echo "Deploying services using Docker Compose..."
    docker stack deploy -c "$DOCKER_COMPOSE_FILE" distributed-notis || exit 1
    echo "Services deployed successfully!"
}

# Check if Docker is running
if ! docker info >/dev/null 2>&1; then
    echo "Docker does not seem to be running, start it first and retry"
    exit 1
fi

# Ask if images should be built and pushed
echo -n "Build and push Docker images? (Y/N): "
read answer
if [[ $answer =~ ^[Yy]$ ]]
then
    build_and_push
else
    echo "Skipping build and push steps..."
fi

# Always deploy
deploy

echo "All operations completed successfully!"
