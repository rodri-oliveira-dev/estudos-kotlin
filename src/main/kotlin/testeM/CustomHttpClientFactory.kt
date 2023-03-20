import io.micronaut.context.annotation.Factory
import io.micronaut.http.client.HttpClientConfiguration
import io.micronaut.http.client.HttpClientFactory
import io.micronaut.http.client.HttpClientOptions
import io.micronaut.runtime.ApplicationConfiguration
import io.micronaut.tracing.brave.instrument.http.TraceHttpClient
import io.opentracing.Tracer
import javax.inject.Singleton

@Factory
class CustomHttpClientFactory(
    private val applicationConfiguration: ApplicationConfiguration,
    private val httpClientConfiguration: HttpClientConfiguration,
    private val tracer: Tracer
) {

    @Singleton
    fun customHttpClient(
        @Named("custom") httpClientOptions: HttpClientOptions
    ): HttpClient {
        val traceHttpClient = TraceHttpClient(httpClientOptions, tracer)
        return HttpClientFactory.create(traceHttpClient, applicationConfiguration, httpClientConfiguration)
    }

    @Named("custom")
    @Singleton
    fun customHttpClientOptions(): HttpClientOptions {
        return HttpClientOptions()
            .apply {
                propagation = TracingHttpClientPropagationThreadLocal.PROPERTY_NAME
            }
    }
}
