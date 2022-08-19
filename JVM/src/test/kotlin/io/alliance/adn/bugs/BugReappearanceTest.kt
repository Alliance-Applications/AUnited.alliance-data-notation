package io.alliance.adn.bugs

import io.alliance.adn.api.kotlin.Dataset
import org.junit.jupiter.api.Test

class BugReappearanceTest {

    @Test
    fun givenValidInput_thenWhitespaceEndShouldNotBreakLexer() {
        val dataset = Dataset.read(BugReappearanceTest::class.java.getResourceAsStream("Bug_00000.adn").reader())
        assert(dataset.size() > 0)
    }
}