#!/bin/bash

APP_NAME="relay-test-endpoint-1.0.0.jar"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_PATH="${SCRIPT_DIR}/${APP_NAME}"
LOG_FILE="${SCRIPT_DIR}/app.log"

if [ ! -f "$APP_PATH" ]; then
    echo "[ERROR] ${APP_NAME} not found: ${APP_PATH}"
    exit 1
fi

# Check if already running
PID=$(pgrep -f "${APP_NAME}")
if [ -n "$PID" ]; then
    echo "[WARN] ${APP_NAME} is already running (PID: ${PID})"
    exit 1
fi

echo "[INFO] Starting ${APP_NAME}..."
nohup java -jar "${APP_PATH}" > "${LOG_FILE}" 2>&1 &

sleep 2
PID=$(pgrep -f "${APP_NAME}")
if [ -n "$PID" ]; then
    echo "[INFO] ${APP_NAME} started successfully (PID: ${PID})"
    echo "[INFO] Log file: ${LOG_FILE}"
else
    echo "[ERROR] Failed to start ${APP_NAME}. Check ${LOG_FILE}"
    exit 1
fi
