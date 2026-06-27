package devops.metadata.syncer;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "devops.metadata.syncer",
        importOptions = ImportOption.OnlyIncludeTests.class
)
class TestArchTest {

    @ArchTest
    static final ArchRule testsShouldNotUseMockito = noClasses()
            .should()
            .dependOnClassesThat()
            .resideInAPackage("org.mockito..");
}