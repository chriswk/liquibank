- Dependencies
  - Kafka
  - ES

All launched from the docker-compose file, 
but you must set `vm.max_map_count=262144` using `sysctl -w vm.max_map_count=262144` 
or setting it in `/etc/sysctl.d/` permanently for elasticsearch to work in docker.
