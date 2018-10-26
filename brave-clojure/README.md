# brave-clojure
Repository for the book "Clojure for the Brave and True"

## Docker

Git source: https://github.com/awolf/brave-clojure.git
Image: https://docs.docker.com/samples/library/clojure/

### Commands

docker run -it --rm clojure /bin/bash

$ docker build -t clojure-noob .
$ docker run -it --rm -v "$PWD":/usr/src/app --name my-clojure-noob clojure-noob bash

$ docker attach my-clojure-noob

$ docker system prune -a

