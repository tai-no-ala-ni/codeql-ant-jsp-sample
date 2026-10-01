#!/usr/bin/env bash
# ============================================================
#  Build all modules in order (Linux version of build_all.bat).
#  CodeQL (build-mode: manual) traces every javac call made here.
# ============================================================
set -euo pipefail
cd "$(dirname "$0")"

echo "[1/3] fetch libraries"
ant -f build-libs.xml

echo "[2/3] framework"
ant -f framework/build.xml clean dist

echo "[3/3] webapp"
ant -f webapp/build.xml clean war

echo "BUILD ALL SUCCESS"
