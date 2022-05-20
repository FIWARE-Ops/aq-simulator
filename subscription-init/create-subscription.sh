#!/bin/sh

curl -X POST "${BROKER_URL}/v2/subscriptions" \
      -H  "accept: application/json" \
      -H  "Content-Type: application/json" \
      -H  "Fiware-Service: ${FIWARE_SERVICE}" \
      -H  "Fiware-ServicePath: ${FIWARE_SERVICE_PATH}" \
      -d "{
            \"description\": \"Subscription for simulated air-quality data\",
            \"subject\": {
                \"entities\": [      {
                  \"idPattern\": \".*\",
                  \"type\": \"${ENTITY_TYPE}\"
                   }
                ]
            },
            \"notification\": {
              \"httpCustom\": {
                \"url\": \"${QUANTUM_LEAP_URL}/v2/notify\",
                \"headers\": {
                  \"fiware-service\" : \"${FIWARE_SERVICE}\",
                  \"fiware-servicepath\" : \"${FIWARE_SERVICE_PATH}\"
                },
                \"attrs\": [
                  \"co\", \"so2\", \"no2\", \"o3\", \"pm1\",\"pm25\",\"Ppm10\"
                ]
              }
            }
          }"