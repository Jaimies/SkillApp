package com.jdevs.timeo.util.time

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.spekframework.spek2.Spek

class ConvertersTest : Spek({

    group("get friendly time") {
        assertThat(getFriendlyTime(89), `is`("1h 29m"))
        assertThat(getFriendlyTime(60), `is`("1h"))
        assertThat(getFriendlyTime(0), `is`(""))
        assertThat(getFriendlyTime(1439), `is`("23h 59m"))
    }

    group("convert float to readable float") {
        assertThat(1f.toReadableFloat(), `is`("1"))
        assertThat(0f.toReadableFloat(), `is`("0"))
        assertThat(0.5f.toReadableFloat(), `is`("0.5"))
    }

    group("get friendly hours from total minutes") {
        assertThat(getFriendlyHours(120), `is`("2"))
        assertThat(getFriendlyHours(0), `is`("0"))
        assertThat(getFriendlyHours(102), `is`("1.7"))
        assertThat(getFriendlyHours(101), `is`("1.7"))
    }

    group("get minutes from hours and minutes") {
        assertThat(getMins(10, 30), `is`(630))
        assertThat(getMins(3, 49), `is`(229))
        assertThat(getMins(18, 17), `is`(1097))
    }
})
