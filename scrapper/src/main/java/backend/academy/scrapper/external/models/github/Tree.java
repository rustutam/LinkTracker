package backend.academy.scrapper.external.models.github;

import java.net.URI;

record Tree(
    String sha,
    URI url
) {
}
