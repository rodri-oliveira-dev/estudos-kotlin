implementation("org.slf4j:slf4j-api:1.7.32")
implementation("org.slf4j:log4j-over-slf4j:1.7.32")


micronaut:
  executors:
    io:
      type: fixed
      nThreads: 75
      mdc: true


import io.micronaut.scheduling.instrument.InvocationInstrumenter
import io.micronaut.scheduling.instrument.ReactiveInvocationInstrumenterFactory

//...

class MdcReactiveInvocationInstrumenterFactory : ReactiveInvocationInstrumenterFactory {
    override fun newReactiveInvocationInstrumenter(): Optional<InvocationInstrumenter> {
        val mdcContextMap = MDC.getCopyOfContextMap()
        return if (mdcContextMap != null) {
            Optional.of(InvocationInstrumenter {
                MDC.setContextMap(mdcContextMap)
            })
        } else {
            Optional.empty()
        }
    }
}
