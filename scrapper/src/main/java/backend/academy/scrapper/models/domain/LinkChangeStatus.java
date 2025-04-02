package backend.academy.scrapper.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class LinkChangeStatus {
    private Link link;
    private boolean hasChanges;
    private List<ChangeInfo> changeInfoList;
}
