package com.deiz.coupon.application

import com.deiz.coupon.domain.Coupon
import com.deiz.coupon.domain.CouponRepository
import com.deiz.coupon.domain.Issuance
import com.deiz.coupon.domain.IssuanceRepository
import com.deiz.coupon.api.dto.CreateCouponRequest
import com.deiz.coupon.support.AlreadyIssuedException
import com.deiz.coupon.support.CouponNotFoundException
import com.deiz.coupon.support.NotStartedException
import com.deiz.coupon.support.SoldOutException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val issuanceRepository: IssuanceRepository
) {

    @Transactional
    fun createCoupon(request: CreateCouponRequest): Coupon {
        val coupon = Coupon(
            name = request.name,
            totalQuantity = request.totalQuantity,
            validityDays = request.validityDays,
            startsAt = request.startsAt,
        )
        return couponRepository.save(coupon)
    }

    @Transactional
    fun issue(couponId: Long, userId: Long): Issuance {
        val coupon = couponRepository.findById(couponId)
            .orElseThrow {(CouponNotFoundException())}

        val now = LocalDateTime.now()

        if (!coupon.isBookingOpen(now)) {
            throw NotStartedException()
        }
        if (coupon.isSoldOut()) {
            throw SoldOutException()
        }
        if (issuanceRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw AlreadyIssuedException()
        }

        coupon.issuedQuantity++

        return issuanceRepository.save(
            Issuance(
                userId = userId,
                couponId = couponId,
                issuedAt = now,
                expiresAt = now.plusDays(coupon.validityDays.toLong())
            )
        )
    }
}