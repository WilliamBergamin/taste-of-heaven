#!/bin/bash
docker image build --tag kingwill/projects:coen424-project-api-1.0 ./API
docker image build --tag kingwill/projects:coen424-project-nginx-1.0 ./Nginx
#docker push kingwill/projects:coen424-project-api-1.0
