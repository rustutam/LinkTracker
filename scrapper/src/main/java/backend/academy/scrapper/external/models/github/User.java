package backend.academy.scrapper.external.models.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

record User(
    String login,
    long id,
    @JsonProperty("node_id") String nodeId,
    @JsonProperty("avatar_url") URI avatarUrl,
    @JsonProperty("gravatar_id") String gravatarId,
    URI url,
    @JsonProperty("html_url") URI htmlUrl,
    @JsonProperty("followers_url") URI followersUrl,
    @JsonProperty("following_url") String followingUrl,
    @JsonProperty("gists_url") String gistsUrl,
    @JsonProperty("starred_url") String starredUrl,
    @JsonProperty("subscriptions_url") URI subscriptionsUrl,
    @JsonProperty("organizations_url") URI organizationsUrl,
    @JsonProperty("repos_url") URI reposUrl,
    @JsonProperty("events_url") String eventsUrl,
    @JsonProperty("received_events_url") URI receivedEventsUrl,
    String type,
    @JsonProperty("site_admin") boolean siteAdmin
) {
}
