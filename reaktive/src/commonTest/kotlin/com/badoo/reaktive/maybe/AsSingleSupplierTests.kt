package com.badoo.reaktive.maybe

import com.badoo.reaktive.test.maybe.TestMaybe
import com.badoo.reaktive.test.single.isError
import com.badoo.reaktive.test.single.test
import com.badoo.reaktive.test.single.value
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AsSingleSupplierTests : MaybeToSingleTests by MaybeToSingleTests<Unit>({ asSingle { Unit } }) {

    private val upstream = TestMaybe<Int?>()
    private val observer = upstream.asSingle { -1 }.test()

    @Test
    fun succeeds_with_upstream_value_WHEN_upstream_succeeded_with_non_null_value() {
        upstream.onSuccess(0)

        assertEquals(0, observer.value)
    }

    @Test
    fun succeeds_with_null_value_WHEN_upstream_succeeded_with_null_value() {
        upstream.onSuccess(null)

        assertEquals(null, observer.value)
    }

    @Test
    fun succeeds_with_default_value_WHEN_upstream_completed() {
        upstream.onComplete()

        assertEquals(-1, observer.value)
    }

    @Test
    fun produces_error_WHEN_supplier_throws_exception() {
        val exception = Exception()

        val observer =
            upstream
                .asSingle { throw exception }
                .test()

        upstream.onComplete()

        assertTrue(observer.isError(exception))
    }
}