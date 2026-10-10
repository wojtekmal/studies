#!/bin/bash

REPO_DIR="/home/wojtekmal/studies"

send_notification() {
    local error_message="$1"
    
    # Required to send desktop notifications from cron
    export DISPLAY=:0
    export DBUS_SESSION_BUS_ADDRESS="unix:path=/run/user/$(id -u)/bus"
    
    notify-send -u critical "Automated Git Push Failed" "$error_message"
}

# 1. Catch directory access failures
cd "$REPO_DIR" || { send_notification "Could not find directory $REPO_DIR"; exit 1; }

# 2. Add changes
git add . || { send_notification "git add failed"; exit 1; }

# 3. Check for staged changes before committing
if ! git diff --staged --quiet; then
    git commit -m "Updating." || { send_notification "git commit failed"; exit 1; }
fi

# 4. Push and capture error output for the notification
if ! PUSH_OUTPUT=$(git push 2>&1); then
    send_notification "git push failed: $PUSH_OUTPUT"
    exit 1
fi
