# metrics-collector

Generic collector os performance readings collectors


# Details

Collectors: 
- [ ] Digital Ocean API: https://docs.digitalocean.com/reference/api/api-reference/#operation/monitoring_get_dropletLoad15Metrics

- [ ] Prometheus API: https://prometheus.io/docs/prometheus/latest/querying/api/

Sinks:
- [ ] Kakfa
- [ ] Minio

Frequency: 
- [ ] Every 10 mins


# Parameters - IN

## Source: Digital Ocean 
https://docs.digitalocean.com/reference/api/api-reference/#tag/Monitoring

Requires: 
- DIGITALOCEAN_TOKEN
- List of HostIds

Limits: 
- Current rate limits are 5000 requests per hour and 250 requests per minute.

## Source: Prometheus

Requires: PROMETHEUS_ADDRESS

Metrics list:

```bash 
CPU, Memory, Disk, i/o, Network

- CPU_Temperature
- sensor_lm_temperature_celsius
- engine_daemon_container_states_containers
- engine_daemon_engine_cpus_cpus
- engine_daemon_engine_memory_bytes
- node_memory_HighFree_bytes
- node_filesystem_free_bytes
```


# Parameters - OUT

- S3: Minio + Parquet

Structure: 
- Metric/Host/Data(Time,Reading)


# Running 

```bash 

docker run -it -w /app \
        --name scala-sandbox \
        -e PROMETHEUS_HOST="127.0.0.1:9090" \
        -v $HOME/_cache/.ivy2:/root/.ivy2  \
        -v $HOME/_cache/.sbt:/root/.sbt \
        -v $HOME/_DEV/dockerhostapi:/source \
        -v $PWD:/app \
        --rm sbtscala/scala-sbt:eclipse-temurin-jammy-8u352-b08_1.8.2_2.12.17 /bin/bash
```