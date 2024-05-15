FROM ghcr.io/dvsa/dvsa-docker-images/java/11/corretto AS base

COPY ./vol-functional-tests .
COPY ~/.m2/settings.xml ~/.m2/settings.xml

CMD ["/bin/sh", "-c", "./vol-functional-tests/run.sh"]


