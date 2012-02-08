#!/bin/zsh

BASE_URL="http://publib.boulder.ibm.com/infocenter/wmqv7/v7r0/topic/com.ibm.mq.amqzao.doc/fm"
END_URL="_.htm"
BASE_SEED=10000
END_SEED=30000
STEP=10

while [[ "${BASE_SEED}" -le "${END_SEED}" ]] ; do
  CURRENT_URL="${BASE_URL}${BASE_SEED}${END_URL}"
  if [[ ! -f "${BASE_SEED}.html" ]] ; then
    wget "$CURRENT_URL" -O"${BASE_SEED}.html"
    CR=$?
    if [[ "$CR" -ne 0 ]] ; then
      rm "${BASE_SEED}.html"
    fi
  fi
  BASE_SEED=$(expr $BASE_SEED + $STEP)
done 
