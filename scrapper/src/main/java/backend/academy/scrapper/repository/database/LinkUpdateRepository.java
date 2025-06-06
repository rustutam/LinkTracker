package backend.academy.scrapper.repository.database;

import backend.academy.scrapper.models.domain.UpdatedLink;
import backend.academy.scrapper.models.domain.ids.LinkId;
import java.util.List;

public interface LinkUpdateRepository {

    List<UpdatedLink> findAll();

    UpdatedLink save(UpdatedLink updatedLink);

    UpdatedLink deleteById(LinkId id);
}
