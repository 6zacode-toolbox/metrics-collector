FROM sbtscala/scala-sbt:eclipse-temurin-jammy-8u352-b08_1.8.2_2.12.17
ADD . /app
RUN cd /app/metrics-collector && sbt -J-Xms2048m -J-Xmx2048m assembly

 
 
FROM cgr.dev/chainguard/jre:openjdk-11
ENV PROMETHEUS_HOST=127.0.0.1:9090
COPY --from=0 /var/tmp/export-jar/app.jar /app/
CMD ["-jar","/app/app.jar"]