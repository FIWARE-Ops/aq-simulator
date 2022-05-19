FROM curlimages/curl:7.83.1

ENV FIWARE_SERVICE=""
ENV FIWARE_SERVICE_PATH="/simulated"
ENV BROKER_URL="http://localhost:1026"
ENV QUANTUM_LEAP_URL="http://localhost:8668"
ENV ENTITY_TYPE="AirQualityObserved"

COPY create-subscription.sh /create-subscription.sh

ENTRYPOINT ["sh", "-c", "/create-subscription.sh"]
