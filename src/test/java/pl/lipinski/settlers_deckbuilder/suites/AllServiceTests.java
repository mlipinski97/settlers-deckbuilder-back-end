package pl.lipinski.settlers_deckbuilder.suites;

import org.junit.jupiter.api.Disabled;
import org.junit.platform.suite.api.ExcludePackages;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import pl.lipinski.settlers_deckbuilder.service.implementation.DeckServiceImplTest;
import pl.lipinski.settlers_deckbuilder.service.implementation.UserServiceImplTest;


@Suite
@SelectPackages("pl.lipinski.settlers_deckbuilder")
public class AllServiceTests {

}
