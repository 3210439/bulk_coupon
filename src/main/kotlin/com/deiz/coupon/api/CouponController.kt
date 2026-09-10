package com.deiz.coupon.api

import com.deiz.coupon.api.dto.CouponResponse
import com.deiz.coupon.api.dto.CreateCouponRequest
import com.deiz.coupon.api.dto.IssuanceResponse
import com.deiz.coupon.application.CouponService
import com.deiz.coupon.domain.CouponRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupons")
class CouponController(
    private val couponService: CouponService,
) {

    @PostMapping
    fun create(@RequestBody request: CreateCouponRequest): ResponseEntity<CouponResponse> {
        val coupon = couponService.createCoupon(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(CouponResponse.from(coupon))
    }

    @PostMapping("/{couponId}/issue")
    fun issue(
        @PathVariable couponId: Long,
        @RequestHeader("X-User-Id") userId: Long,
    ): IssuanceResponse {
        val issuance = couponService.issue(couponId, userId)
        return IssuanceResponse.from(issuance)
    }
}