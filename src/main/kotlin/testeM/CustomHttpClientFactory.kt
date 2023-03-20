implementation("org.slf4j:slf4j-api:1.7.32")
implementation("org.slf4j:log4j-over-slf4j:1.7.32")


micronaut:
  executors:
    io:
      type: fixed
      nThreads: 75
      mdc: true

import io.micronaut.context.annotation.Factory
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import javax.inject.Singleton

@Factory
class HttpClientFactory {

    @Singleton
    fun httpClient(@Client("https://example.com") baseUrl: String): HttpClient {
        return HttpClient.create(baseUrl)
    }
}
