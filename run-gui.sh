#!/usr/bin/env bash
# Helper to run the GUI; will use xvfb-run automatically when no DISPLAY is set.

set -euo pipefail

JAR="target/swingy-1.0-SNAPSHOT-shaded.jar"
if [ ! -f "$JAR" ]; then
  JAR="target/swingy-1.0-SNAPSHOT.jar"
fi

if [ ! -f "$JAR" ]; then
  echo "Jar not found in target/. Build first with: mvn -DskipTests package"
  exit 1
fi

# Determine java binary to use. Prefer a full JDK that includes AWT native libs.
if [ -n "${JAVA_HOME:-}" ] && [ -x "${JAVA_HOME}/bin/java" ]; then
  JAVA_BIN="${JAVA_HOME}/bin/java"
else
  JAVA_BIN="$(which java 2>/dev/null || true)"
fi

# If the selected java is from a JDK that lacks desktop natives (e.g. openjdk-21 minimal),
# prefer a bundled java-17 installation if available.
if [ -x "/usr/lib/jvm/java-17-openjdk-amd64/bin/java" ]; then
  JAVA_BIN="/usr/lib/jvm/java-17-openjdk-amd64/bin/java"
fi

# Set LD_LIBRARY_PATH so JVM's libjvm.so can be found by native libraries
JAVA_DIR="$(dirname "${JAVA_BIN}")/.."
LD_LIB_PATH_CANDIDATE="${JAVA_DIR}/lib/server"
if [ -d "$LD_LIB_PATH_CANDIDATE" ]; then
  export LD_LIBRARY_PATH="$LD_LIB_PATH_CANDIDATE:${LD_LIBRARY_PATH:-}"
fi

if [ -z "${DISPLAY:-}" ]; then
  if command -v xvfb-run >/dev/null 2>&1; then
    echo "No DISPLAY detected — launching under xvfb-run using $JAVA_BIN..."
    xvfb-run --auto-servernum --server-args='-screen 0 1024x768x24' "$JAVA_BIN" -Djava.awt.headless=false -jar "$JAR"
  else
    echo "No DISPLAY and xvfb-run is not installed. Install xvfb or run with an X server."
    echo "On Ubuntu: sudo apt install xvfb"
    exit 1
  fi
else
  # If DISPLAY is set but not responding, fall back to xvfb-run
  if command -v xdpyinfo >/dev/null 2>&1; then
    if xdpyinfo -display "$DISPLAY" >/dev/null 2>&1; then
      echo "DISPLAY $DISPLAY is available — launching directly with $JAVA_BIN."
      "$JAVA_BIN" -Djava.awt.headless=false -jar "$JAR"
      exit $?
    else
      echo "DISPLAY $DISPLAY is set but not responding — falling back to xvfb-run."
      if command -v xvfb-run >/dev/null 2>&1; then
        xvfb-run --auto-servernum --server-args='-screen 0 1024x768x24' "$JAVA_BIN" -Djava.awt.headless=false -jar "$JAR"
        exit $?
      else
        echo "xvfb-run not available; cannot start GUI."
        exit 1
      fi
    fi
  else
    echo "xdpyinfo not available to test DISPLAY; attempting to launch directly with $JAVA_BIN."
    "$JAVA_BIN" -Djava.awt.headless=false -jar "$JAR"
  fi
fi
