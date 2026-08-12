package devops.platform.infrastructure.outbound.database;

import devops.platform.domain.models.Project;
import devops.platform.domain.models.ProjectContact;
import devops.platform.domain.models.randomizers.ProjectContactRandomizer;
import devops.platform.domain.outbound.ProjectContactInventory;
import devops.platform.infrastructure.OutboundDatabaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectContactInventoryDatabaseAdapterTest extends OutboundDatabaseIntegrationTest {

    @Autowired
    private ProjectContactInventory sut;

    @ParameterizedTest
    @NullSource
    @EmptySource
    void createAll_shouldNotInsert_whenContactsIsNull(List<ProjectContact> contacts) {
        // Arrange
        Project project = createProject();
        // Act
        sut.createAll(contacts, project);
        // Assert
        List<ProjectContact> projectContacts = sut.findAllByProject(project);
        assertThat(projectContacts)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void createAll() {
        // Arrange
        Project project = createProject();
        ProjectContact contact1 = ProjectContactRandomizer.random();
        ProjectContact contact2 = ProjectContactRandomizer.random();
        ProjectContact contact3 = ProjectContactRandomizer.random();
        List<ProjectContact> contacts = List.of(
                contact1,
                contact2,
                contact3
        );
        // Act
        sut.createAll(contacts, project);
        // Assert
        List<ProjectContact> projectContacts = sut.findAllByProject(project);
        assertThat(projectContacts)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .contains(contact1, contact2, contact3);
    }

}
