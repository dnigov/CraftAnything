#!/bin/sh
if command -v gradle >/dev/null 2>&1; then exec gradle "$@"; fi
echo "Gradle is not installed. Install Gradle 8+ or generate the wrapper with: gradle wrapper" >&2
exit 1
