#!/bin/sh
DIR="$(dirname "$(readlink -f "$0")")"
$DIR/test_unit.sh && $DIR/test_integration.sh
