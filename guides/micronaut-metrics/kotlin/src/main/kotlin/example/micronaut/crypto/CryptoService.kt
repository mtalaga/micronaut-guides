package example.micronaut.crypto

import example.micronaut.ScheduledConfiguration
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

@Singleton // <1>
class CryptoService constructor(
    private val priceClient: PriceClient, // <2>
    private val scheduledConfiguration: ScheduledConfiguration,
    meterRegistry: MeterRegistry) {

    private val LOG = LoggerFactory.getLogger(javaClass.name)

    private val checks: Counter
    private val time: Timer
    private val latestPriceUsd = AtomicInteger(0)

    init {
        checks = meterRegistry.counter("bitcoin.price.checks") // <3>
        time = meterRegistry.timer("bitcoin.price.time") // <4>
        meterRegistry.gauge("bitcoin.price.latest", latestPriceUsd) // <5>
    }

    @Scheduled(fixedRate = "\${crypto.update-frequency:1h}",
            initialDelay = "\${crypto.initial-delay:0s}") // <6>
    fun updatePrice() {
        if (scheduledConfiguration.isEnabled()) {
            time.record { // <7>
                try {
                    checks.increment() // <8>
                    latestPriceUsd.set(priceClient.latestInUSD().price.toInt()) // <9>
                } catch (e: Exception) {
                    LOG.error("Problem checking price", e)
                }
            }
        }
    }
}
