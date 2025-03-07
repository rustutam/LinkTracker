package backend.academy.scrapper.models.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class RepositoryTables {
    private List<Long> users = new ArrayList<>();
    private List<UserLinksEntity> userLinksEntities = new ArrayList<>();
    private List<LinksEntity> linksEntities = new ArrayList<>();
    private List<InfoEntity> infoEntities = new ArrayList<>();
}
