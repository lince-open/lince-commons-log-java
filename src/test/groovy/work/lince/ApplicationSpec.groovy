package work.lince

import spock.lang.Specification
import spock.lang.Unroll

class ApplicationSpec extends Specification {

    def setup() {
    }

    @Unroll
    def "with #var"() {
        given:
            def param

        when:
            def result

        then:
            true == true

        where:
            var     | _
            "value" | _

    }

}