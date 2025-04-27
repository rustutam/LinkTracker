package backend.academy.scrapper.models.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LinkChangeStatus {
    private Link link;
    private boolean hasChanges;
    private List<ChangeInfo> changeInfoList;
}
