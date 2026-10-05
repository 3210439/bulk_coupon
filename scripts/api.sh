#!/usr/bin/env bash
set -e
BASE="${BASE_URL:-http://localhost:8080}"

COUPON_RES=$(curl -sS -X POST "$BASE/api/coupons" \
  -H 'Content-Type: application/json' \
  -d '{"name":"summer_promotion","totalQuantity":100,"validityDays":7}')
echo "coupon response: $COUPON_RES"
COUPON=$(echo "$COUPON_RES" | jq -r '.id')
echo "COUPON=$COUPON"

ISSUE_RES=$(curl -sS -i -X POST "$BASE/api/coupons/$COUPON/issue" -H 'X-User-Id: 1')
echo "issue response: $ISSUE_RES"