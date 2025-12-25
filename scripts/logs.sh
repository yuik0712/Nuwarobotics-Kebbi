#!/usr/bin/env bash
set -euo pipefail

# Colorize HelloKebbi logs by status.
# Usage: bash scripts/logs.sh [TAG]
# Default TAG is HelloKebbi.

TAG="${1:-HelloKebbi}"

# Try to clear buffer (ignore errors if device not connected yet)
adb logcat -c >/dev/null 2>&1 || true

# Follow only our tag at INFO+ and silence everything else
adb logcat -v time "${TAG}:I" '*:S' | awk '
  BEGIN {
    green = "\033[32m"; red = "\033[31m"; yellow = "\033[33m"; reset = "\033[0m";
  }
  {
    line = $0;
    if (line ~ / failure:/) {
      print red line reset;
    } else if (line ~ / success$/) {
      print green line reset;
    } else if (line ~ / start$/) {
      print yellow line reset;
    } else {
      print line;
    }
    fflush();
  }
'

