#!/bin/bash

APP_NAME="relay-test-endpoint-1.0.0.jar"

PID=$(pgrep -f "${APP_NAME}")

if [ -z "$PID" ]; then
    echo "[WARN] ${APP_NAME} is not running"
    exit 0
fi

echo "[INFO] Stopping ${APP_NAME} (PID: ${PID})..."
kill "$PID"

# Wait for graceful shutdown
for i in $(seq 1 10); do
    if ! pgrep -f "${APP_NAME}" > /dev/null 2>&1; then
        echo "[INFO] ${APP_NAME} stopped successfully"
        exit 0
    fi
    sleep 1
done

# Force kill if still running
echo "[WARN] Graceful shutdown timed out. Force killing..."
kill -9 "$PID"
echo "[INFO] ${APP_NAME} force killed"
